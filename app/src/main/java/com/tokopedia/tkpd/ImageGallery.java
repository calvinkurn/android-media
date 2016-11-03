package com.tokopedia.tkpd;


import android.*;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tkpd.library.ui.animation.FlipAnimation;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.customadapter.ImageGalleryAdapter;
import com.tokopedia.tkpd.customadapter.ImageGalleryAlbumAdapter;
import com.tokopedia.tkpd.util.RequestPermissionUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImageGallery extends TActivity {
    public interface ImageGalleryListener {
        public void onPictureClicked(int position);
    }

    private ImageGalleryListener mListener;
    public static final int TOKOPEDIA_GALLERY = 111;
    public static final int RESULT_CODE = 323;
    public static final String EXTRA_URL = "image_url";
    private static final String BASE_URL = "DCIM";
    private List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");
    private List<String> FORBIDEN_FILE = Arrays.asList("android", "tokopedia", "application");

    private List<ListImageHolder> mAlbumDatas = new ArrayList<ListImageHolder>();
    private List<ListImageHolder> mPhotoDatas = new ArrayList<ListImageHolder>();
    private List<File> mPhotoFile = new ArrayList<File>();

    private ImageGalleryAlbumAdapter mAlbumAdapter;
    private ImageGalleryAdapter mPhotoAdapter;
    private RelativeLayout rootView;// rootview for animation
    private View topView;
    private View bottomView;
    private GridView mGridView;        // GridView for viewing list of Photo
    private ListView mListView;        // ListView for viewing list of Album Photo
    private boolean isAlbum;        // true - state in Album folder || false - state in Photo folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.picture_galery_layout);
        initData();
        initView();
        initAdapter();
    }

    private void initData() {
        isAlbum = true; // first open must be in Album Folder
        mAlbumDatas.clear();
        mPhotoDatas.clear();
        getAllAlbumData(new File(
                android.os.Environment.getExternalStorageDirectory()
                        + File.separator
                        + BASE_URL
        ).getParentFile().getAbsoluteFile());

        mListener = new ImageGalleryListener() {
            @Override
            public void onPictureClicked(int position) {
                if (isAlbum) {
                    isAlbum = false;
                    mPhotoDatas.clear();
                    mPhotoDatas.addAll(
                            sortingDateFile(
                                    getAllImageChild(new File(mAlbumDatas.get(position).getUrl())
                                    )
                            )
                    );
                    mPhotoAdapter.notifyDataSetChanged();
                    flipCard();
                } else {
                    finish(mPhotoDatas.get(position).getUrl());
                }
            }
        };

    }

    private void initView() {
        rootView = (RelativeLayout) findViewById(R.id.gallery_rootview);
        topView = (View) findViewById(R.id.gallery_topview);
        bottomView = (View) findViewById(R.id.gallery_bottomview);
        mGridView = (GridView) findViewById(R.id.gallery_gridview);
        mListView = (ListView) findViewById(R.id.gallery_listview);
    }

    private void initAdapter() {
//		mAlbumAdapter = new ImageGalleryAlbumAdapter(this, mAlbumDatas, mListener);
//		mPhotoAdapter = new ImageGalleryAdapter(this, mPhotoDatas, mListener);

        mListView.setAdapter(mAlbumAdapter);
        mGridView.setAdapter(mPhotoAdapter);
    }

    private void getAllAlbumData(File directory) {
        if (directory.isDirectory()) {
            File[] listFiles = directory.listFiles(fileFilter);
            if (listFiles != null && listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].isDirectory()) {
                        if (isPictureFolder(listFiles[i].getAbsoluteFile())) {
                            //kalo dia punya anak gambar
                            ListImageHolder tempAlbum = new ListImageHolder();
                            tempAlbum.setFolderName(listFiles[i].getName());
                            tempAlbum.setUrl(listFiles[i].getAbsolutePath());
                            tempAlbum.setFirstPhotoURL(getFirstImageChild(listFiles[i].getAbsoluteFile()));
                            tempAlbum.setFile(listFiles[i].getAbsoluteFile());
                            mAlbumDatas.add(tempAlbum);
                        }
                        getAllAlbumData(listFiles[i].getAbsoluteFile());
                    }
                }
            }
        }
    }

    private void flipCard() {
        FlipAnimation flipAnimation = new FlipAnimation(topView, bottomView);
        if (topView.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootView.startAnimation(flipAnimation);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // FW - buat file filtering Album secara kesuluruhan
    private FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                if (isNoMediaFolder(pathname)) {
                    return false;
                } else {
                    return !isForbiddenFile(pathname.getName());
                }
            } else {
                return false;
            }
        }
    };

    // FW - buat filtering apa folder punya file .nomedia
    private boolean isNoMediaFolder(File pathname) {
        File[] child = pathname.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.equalsIgnoreCase(".nomedia"))
                    return true;
                else
                    return false;
            }
        });
        if (child != null && child.length > 0)
            return true;
        else
            return false;
    }

    // FW - buat filtering folder punya file image
    private boolean isPictureFolder(File pathname) {
        File[] child = pathname.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return isSupportedFile(pathname.getAbsolutePath());
            }
        });
        if (child != null && child.length > 0)
            return true;
        else
            return false;
    }

    // FW - buat filtering ekstension sesuai dengan ekstension gambar
    private boolean isSupportedFile(String filePath) {

        String ext = filePath.substring(
                (filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }

    // FW - buat filtering folder yang dilarang
    private boolean isForbiddenFile(String fileName) {
        if (FORBIDEN_FILE.contains(fileName.toLowerCase(Locale.getDefault())) || isSystemFolderName(fileName))
            return true;
        else
            return false;
    }

    private boolean isSystemFolderName(String fileName) {
        return (fileName.startsWith("."));
    }

    //buat sorting file berdasarkan date - FW
    private List<ListImageHolder> sortingDateFile(List<ListImageHolder> files) {
        List<ListImageHolder> oldFiles = files;
        List<ListImageHolder> newFiles = new ArrayList<ListImageHolder>();
        long currTime;
        int lengthFile = oldFiles.size();
        int newFileIndex = 0;
        Log.e("", "size real = " + oldFiles.size());
        for (int j = 0; j < lengthFile; j++) {
            currTime = -1;
            for (int i = 0; i < oldFiles.size(); i++) {
                if (currTime < oldFiles.get(i).getFile().lastModified() || currTime == -1) {
                    currTime = oldFiles.get(i).getFile().lastModified();
                    newFileIndex = i;
                }
            }
            newFiles.add(oldFiles.get(newFileIndex));
            oldFiles.remove(newFileIndex);
            Log.e("", "J = " + j);
            Log.e("", "size now = " + lengthFile);
        }
        return newFiles;
    }

    // FW - buat mendapatkan url image pertama dalam folder
    private String getFirstImageChild(File pathname) {
        File[] child = pathname.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return isSupportedFile(pathname.getAbsolutePath());
            }
        });
        if (child != null && child.length > 0)
            return Uri.decode(Uri.fromFile(child[0]).toString());
        else
            return null;
    }

    // FW - buat mendapatkan url image pertama dalam folder
    private List<ListImageHolder> getAllImageChild(File pathname) {
        List<ListImageHolder> listImageChild = new ArrayList<ListImageHolder>();
        File[] child = pathname.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return isSupportedFile(pathname.getAbsolutePath());
            }
        });
        if (child != null && child.length > 0) {
            for (File file : child) {
                ListImageHolder childTemp = new ListImageHolder();
                childTemp.setFirstPhotoURL(Uri.decode(Uri.fromFile(file).toString()));
                childTemp.setFolderName(file.getName());
                childTemp.setUrl(file.getAbsolutePath());
                childTemp.setFile(file.getAbsoluteFile());
                listImageChild.add(childTemp);
            }
            return listImageChild;
        } else
            return null;
    }

    @Override
    public void finish() {
        finish("");
    }

    //Finish buat kirim intent brupa image url
    public void finish(String url) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_URL, url);
        setResult(RESULT_CODE, intent);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (isAlbum) {
            super.onBackPressed();
        } else {
            isAlbum = true;
            flipCard();
        }
    }

    public class ListImageHolder {
        private String firstPhotoURL;
        private String folderName;
        private String url;
        private File file;

        public String getFirstPhotoURL() {
            return firstPhotoURL;
        }

        public void setFirstPhotoURL(String firstPhotoURL) {
            this.firstPhotoURL = firstPhotoURL;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RequestPermissionUtil.checkHasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            finish();
        }
    }
}
