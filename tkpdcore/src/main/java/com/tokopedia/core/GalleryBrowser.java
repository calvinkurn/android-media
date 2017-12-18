package com.tokopedia.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tkpd.library.ui.animation.FlipAnimation;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.ImageGalleryAdapter;
import com.tokopedia.core.customadapter.ImageGalleryAlbumAdapter;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryImpl;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class GalleryBrowser extends TActivity implements ImageGalleryView {

    public static final int TOKOPEDIA_GALLERY = 111;
    public static final int RESULT_CODE = 323;
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_URLS = "image_urls";
    ImageGalleryImpl imageGallery;
    private ArrayList<ListImageHolder> BucketList = new ArrayList<ListImageHolder>();
    private ArrayList<ListImageHolder> BucketImageList = new ArrayList<ListImageHolder>();
    private List<FolderModel> test;
    private ImageGalleryAlbumAdapter mAlbumAdapter;
    private ImageGalleryAdapter mPhotoAdapter;
    private RelativeLayout rootView;// rootview for animation
    private View topView;
    private View bottomView;
    private GridView mGridView;        // GridView for viewing list of Photo
    private ListView mListView;        // ListView for viewing list of Album Photo
    private boolean isAlbum = true;        // true - state in Album folder || false - state in Photo folder

    public static ListImageHolder fromFolderModel(FolderModel folderModel) {
        ListImageHolder listImageHolder = new ListImageHolder();
        listImageHolder.setFolderName(folderModel.getPath());
        listImageHolder.setFirstPhotoURL("file://" + folderModel.getImageModels().get(0).getPath());
        return listImageHolder;
    }

    public static ListImageHolder fromFolderModelEl(ImageModel imageModel) {
        ListImageHolder listImageHolder = new ListImageHolder();
        listImageHolder.setFirstPhotoURL("file://" + imageModel.getPath());
        listImageHolder.setUrl(imageModel.getPath());
        return listImageHolder;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GALLERY_BROWSE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_gallery_browser);
        rootView = (RelativeLayout) findViewById(R.id.gallery_rootview);
        topView = findViewById(R.id.gallery_topview);
        bottomView = findViewById(R.id.gallery_bottomview);
        mGridView = (GridView) findViewById(R.id.gallery_gridview);
        mListView = (ListView) findViewById(R.id.gallery_listview);
        if (RequestPermissionUtil.checkHasPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            imageGallery = new ImageGalleryImpl(this);
            test = imageGallery.fetchImageUsingDb();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RequestPermissionUtil.checkHasPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            finish();
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

    @Override
    public void onBackPressed() {
        if (isAlbum) {
            super.onBackPressed();
        } else {
            isAlbum = true;
            flipCard();
        }
    }

    @Override
    public void fetchImageFromDb() {

    }

    @Override
    public void fetchImageFromDb(String folderPath) {

    }

    @Override
    public void loadData(List<FolderModel> models) {
        BucketList = new ArrayList<>();
        for (FolderModel folderModel : test) {
            BucketList.add(fromFolderModel(folderModel));
        }
        mAlbumAdapter = new ImageGalleryAlbumAdapter(this, BucketList, null);
        mPhotoAdapter = new ImageGalleryAdapter(this, BucketImageList, null);
        mListView.setAdapter(mAlbumAdapter);
        mGridView.setAdapter(mPhotoAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isAlbum = false;
                BucketImageList.clear();
                List<ImageModel> imageModels = FolderModel.searchImageModels(test, BucketList.get(position).getFolderName());
                for (ImageModel imageModel : imageModels) {
                    BucketImageList.add(fromFolderModelEl(imageModel));
                }
                mPhotoAdapter.notifyDataSetChanged();
                flipCard();
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(IMAGE_URL, BucketImageList.get(position).getUrl());
                setResult(RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    public void moveToGallery(List<ImageModel> imageModels, int selection) { /* this is just for nothing */}

    @Override
    public void moveToGallery(int position, int maxSelection) { /* this is just for nothing */}

    @Override
    public void initFragment(String FRAGMENT_TAG) {

    }

    @Override
    public void fetchExtras(Intent intent) {

    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG) {

    }

    @Override
    public ModalMultiSelectorCallback getMultiSelectorCallback(String FRAGMENT_TAG) {
        return null;
    }

    @Override
    public void sendResultImageGallery(String path) {

    }

    @Override
    public void sendResultImageGallery(List<String> paths) {
        /* DO NOTHING */
    }

    @Override
    public ActionMode showActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void retrieveData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> dataAlbum) {
        /* DO NOTHING */
    }

    @Override
    public void retrieveItemData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> dataAlbum) {
		/* DO NOTHING */
    }

    @Override
    public boolean isNeedPermission() {
        return true;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public static class ListImageHolder {
        private String firstPhotoURL;
        private String folderName;
        private String url;

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


    }
}
