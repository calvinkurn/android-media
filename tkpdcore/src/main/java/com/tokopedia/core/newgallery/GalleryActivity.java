package com.tokopedia.core.newgallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.fragment.ImageGalleryAlbumFragment;
import com.tokopedia.core.myproduct.fragment.ImageGalleryFragment;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.presenter.ImageGallery;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.newgallery.presenter.ImageGalleryImpl;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by sebastianuskh on 12/30/16.
 */
@RuntimePermissions
public class GalleryActivity extends TActivity implements ImageGalleryView {
    public static final String ADD_PRODUCT_IMAGE_LOCATION = "ADD_PRODUCT_IMAGE_LOCATION";
    public static final String FORCE_OPEN_CAMERA = "FORCE_OPEN_CAMERA";
    public static final String MAX_IMAGE_SELECTION = "MAX_IMAGE_SELECTION";
    public static final int ADD_PRODUCT_IMAGE_LOCATION_DEFAULT = 0;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int INSTAGRAM_SELECT_REQUEST_CODE = 101;

    public static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";
    public static final String PRODUCT_SOC_MED_DATA = "PRODUCT_SOC_MED_DATA";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_PATH_CAMERA = "IMAGE_PATH_CAMERA";
    public static final String IS_CAMERA_OPEN = "IS_CAMERA_OPEN";
    public static final String TOKOPEDIA = "Tokopedia";

    String FRAGMENT;
    int position;

    ImageGallery imageGallery;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private FragmentManager supportFragmentManager;
    private Unbinder unbinder;

//    @BindView(R2.id.fab)
//    FloatingActionButton fab;

    private boolean forceOpenCamera;
    private int maxSelection;
    private Fragment galeryActivityFragment;
    private String imagePathCamera;
    private boolean isCameraOpen = false;

    private FabSpeedDial fabSpeedDial;

    private TkpdProgressDialog progressDialog;


    /**
     * Call this to get image from image gallery
     *
     * @param context non null object
     */
    public static void moveToImageGallery(AppCompatActivity context, int position) {
        moveToImageGalleryCamera(context, position, false, -1);
    }

    /**
     * Call this to get image from image gallery and can select more that one.
     *
     * @param context non null object
     */
    public static void moveToImageGallery(AppCompatActivity context, int position, int maxSelection) {
        moveToImageGalleryCamera(context, position, false, maxSelection);
    }

    /**
     * Call this to get image from image gallery
     * and force open camera.
     *
     * @param context non null object
     */
    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera, int maxImageSelection){
        Intent imageGallery = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
        bundle.putString(FRAGMENT_TO_SHOW, ImageGalleryAlbumFragment.FRAGMENT_TAG);
        bundle.putBoolean(FORCE_OPEN_CAMERA, forceOpenCamera);
        bundle.putInt(MAX_IMAGE_SELECTION, maxImageSelection);
        imageGallery.putExtras(bundle);

        //[START] This one is old one
//        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        //[END] This one is old one
        context.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + TOKOPEDIA + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + System.currentTimeMillis() / 1000L + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onRestoreSavedState(savedInstanceState);

        fetchExtras(getIntent());
        setContentView(R.layout.activity_gallery);
        unbinder = ButterKnife.bind(this);

        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(ImageGalleryView.TAG, "back stack changed [count : " + backCount);
                if (backCount == 0) {
                    finish();
                }
            }
        });


        imageGallery = new ImageGalleryImpl(this);

//        if (fab != null)
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    GalleryActivityPermissionsDispatcher.onFabClickedWithCheck(GalleryActivity.this, view);
//                }
//            });

        fabSpeedDial.setListenerFabClick(new ListenerFabClick() {
            @Override
            public void onFabClick() {
                if (!fabSpeedDial.isShown()) {
                    fabSpeedDial.setVisibility(View.VISIBLE);
                }
            }
        });

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_instagram) {
                    if(getApplication() instanceof TkpdCoreRouter)
                        ((TkpdCoreRouter)getApplication()).startInstopedActivityForResult(GalleryActivity.this,
                                INSTAGRAM_SELECT_REQUEST_CODE, maxSelection);
                } else if (id == R.id.action_camera) {
                    onCameraClicked();
                }
                return false;
            }
        });
    }

    private void onCameraClicked() {
        GalleryActivityPermissionsDispatcher.onFabClickedWithCheck(this);
    }

    private void onRestoreSavedState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imagePathCamera = savedInstanceState.getString(IMAGE_PATH_CAMERA);
            isCameraOpen = savedInstanceState.getBoolean(IS_CAMERA_OPEN, false);
        }
    }

    @Override
    public void fetchImageFromDb() {
        if (imageGallery != null) {
            imageGallery.fetchImageUsingDb(this);
        }
    }

    @Override
    public void loadData(List<FolderModel> models) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageGalleryAlbumFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryAlbumFragment) {
            ImageGalleryAlbumFragment imageGalleryAlbumFragment = (ImageGalleryAlbumFragment) fragment;
            // Load Data
            imageGalleryAlbumFragment.loadData(models);
        }
    }

    @Override
    public void moveToGallery(List<ImageModel> imageModels, int maxSelection) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment) {
            Log.d(TAG, messageTAG + ImageGalleryFragment.FRAGMENT_TAG + " already created!!!");
        } else {
            Fragment imageGalleryFragment = ImageGalleryFragment.newInstance(imageModels, maxSelection);
            moveToFragment(imageGalleryFragment, true, ImageGalleryFragment.FRAGMENT_TAG);
        }
    }

    @Override
    public void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which fragment should be created
            String fragment = intent.getExtras().getString(FRAGMENT_TO_SHOW);
            if (fragment != null) {
                switch (fragment) {
                    case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                    case ImageGalleryFragment.FRAGMENT_TAG:
                        FRAGMENT = fragment;
                        break;
                }
            }

            position = intent.getExtras().getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            forceOpenCamera = intent.getExtras().getBoolean(FORCE_OPEN_CAMERA, false);
            maxSelection = intent.getExtras().getInt(MAX_IMAGE_SELECTION, -1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SessionHandler.isFirstTimeAskedPermissionStorage(GalleryActivity.this)
                || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            GalleryActivityPermissionsDispatcher.checkPermissionWithCheck(this);
        else
            RequestPermissionUtil.onFinishActivityIfNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);

        if (forceOpenCamera && checkNotNull(fabSpeedDial)) {
            // fabSpeedDial.performClick();
            onCameraClicked();
        }
    }

    @Override
    public void initFragment(String FRAGMENT_TAG) {
        switch (FRAGMENT_TAG) {
            case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                if (supportFragmentManager.findFragmentByTag(ImageGalleryAlbumFragment.FRAGMENT_TAG) == null) {
                    if (maxSelection == -1)
                        galeryActivityFragment = ImageGalleryAlbumFragment.newInstance();
                    else {
                        galeryActivityFragment = ImageGalleryAlbumFragment.newInstance(maxSelection);
                    }
                    moveToFragment(galeryActivityFragment, true, ImageGalleryAlbumFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + ImageGalleryAlbumFragment.FRAGMENT_TAG + " is already created");
                }
                break;
            case ImageGalleryFragment.FRAGMENT_TAG:
                Log.d(TAG, messageTAG + ImageGalleryFragment.FRAGMENT_TAG + " can be accessed from " + ImageGalleryAlbumFragment.FRAGMENT_TAG);
                break;
        }
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.add_product_container, fragment, TAG);
        if (isAddtoBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sendResultImageGallery(String path) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment && path != null) {
            Intent intent = new Intent();
            intent.putExtra(GalleryBrowser.IMAGE_URL, path);
            intent.putExtra(ADD_PRODUCT_IMAGE_LOCATION, position);
            setResult(GalleryBrowser.RESULT_CODE, intent);
            finish();
        }
    }

    @Override
    public void sendResultImageGallery(List<String> paths) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment && checkCollectionNotNull(paths)) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(GalleryBrowser.IMAGE_URLS, new ArrayList<String>(paths));
            intent.putExtra(ADD_PRODUCT_IMAGE_LOCATION, position);
            setResult(GalleryBrowser.RESULT_CODE, intent);
            finish();
        }
    }

    @Override
    public ActionMode showActionMode(ActionMode.Callback callback) {
        if (checkNotNull(callback))
            return startSupportActionMode(callback);
        else
            return null;    }

    @Override
    public ModalMultiSelectorCallback getMultiSelectorCallback(String FRAGMENT_TAG) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null && fragment.isVisible() && fragment instanceof ImageGalleryFragment) {
            return ((ImageGalleryFragment) fragment).getmDeleteMode();
        }

//        if(fragment !=null && fragment.isVisible() && fragment instanceof AddProductFragment){
//            return ((AddProductFragment)fragment).getmDeleteMode();
//        }
        return null;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onFabClicked() {
        switch (FRAGMENT) {
            case ImageGalleryAlbumFragment.FRAGMENT_TAG:
            case ImageGalleryFragment.FRAGMENT_TAG:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_REMOVED)) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File outputMediaFile = getOutputMediaFile();
                        if (!isCameraOpen) {
                            isCameraOpen = true;
                            imagePathCamera = outputMediaFile.getAbsolutePath();
                            Uri fileuri = MethodChecker.getUri(GalleryActivity.this, outputMediaFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
                            startActivityForResult(takePictureIntent,
                                    CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                    }
                } else {
                    WarningDialog();
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_PATH_CAMERA, imagePathCamera);
        outState.putBoolean(IS_CAMERA_OPEN, isCameraOpen);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void checkPermission() {
        CommonUtils.dumper("NISNISNIS GaleryActivity Storage");
    }

    public void WarningDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.dialog_no_memory_card));
        myAlertDialog.setPositiveButton(getString(R.string.title_ok), null);

        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            switch (resultCode){
                case RESULT_CANCELED:
                    forceOpenCamera = false;
                    finish();
                    break;
                case RESULT_OK:
                    switch (FRAGMENT) {
                        case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                        case ImageGalleryFragment.FRAGMENT_TAG:
                            if (imagePathCamera != null) {
                                Intent intent = new Intent();
                                intent.putExtra("image_url", imagePathCamera);
                                intent.putExtra(ADD_PRODUCT_IMAGE_LOCATION, position);
                                setResult(GalleryBrowser.RESULT_CODE, intent);
                                finish();
                            }
                            break;
                    }
                    break;
            }
        }
        else if (requestCode == INSTAGRAM_SELECT_REQUEST_CODE){
            switch (resultCode){
                case RESULT_OK:
                    SparseArray<InstagramMediaModel> instagramMediaModelSparseArray
                            = Parcels.unwrap(data.getParcelableExtra(PRODUCT_SOC_MED_DATA));

                    //[START] convert instagram model to new models
                    List<InstagramMediaModel> images = new ArrayList<>();
                    images.addAll(fromSparseArray(instagramMediaModelSparseArray));
                    ArrayList<String> paths = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        paths.add(images.get(i).standardResolution);
                    }

                    convertHttpPathToLocalPath(paths);
                    break;
                default:
                    // no op
                    break;
            }
        }
    }

    private Observable<List<File>> downloadImages(final List<String> urls){
        return Observable.from(urls)
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String url) {
                        return downloadObservable(url).first();
                    }
                }).toList();
    }

    @NonNull
    private Observable<File> downloadObservable(String url) {
        return Observable.just(url)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String url) {
                        FutureTarget<File> future = Glide.with(GalleryActivity.this)
                                .load(url)
                                .downloadOnly(4096, 2160);
                        File photo = null;
                        try {
                            File cacheFile = future.get();
                            photo = writeImageToTkpdPath(cacheFile);
                            Log.d(TAG, messageTAG + "path -> " + (photo != null ? photo.getAbsolutePath() : "kosong"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                        return photo;
                    }
                });
    }

    public static File writeImageToTkpdPath(File source) {
        InputStream inStream = null;
        OutputStream outStream = null;
        File dest = null;
        try {

            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            dest = new File(directory.getAbsolutePath() + "/image.jpg");

            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            Log.d(TAG, "File is copied successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public void convertHttpPathToLocalPath(List<String> urls) {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        progressDialog.showDialog();

        downloadImages(urls)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<List<File>>() {
                            @Override
                            public void onCompleted() {
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                            @Override
                            public void onNext(List<File> files) {
                                if (progressDialog != null && progressDialog.isProgress()) {
                                    progressDialog.dismiss();
                                }
                                Intent intent = new Intent();
                                ArrayList<String> localPaths = new ArrayList<>();
                                for (int i = 0, sizei = files.size(); i < sizei; i++) {
                                    localPaths.add(files.get(i).getAbsolutePath());
                                }
                                intent.putStringArrayListExtra(GalleryBrowser.IMAGE_URLS, localPaths);
                                intent.putExtra(ADD_PRODUCT_IMAGE_LOCATION, position);
                                setResult(GalleryBrowser.RESULT_CODE, intent);
                                finish();
                            }
                        }
                );
    }

    private List<InstagramMediaModel> fromSparseArray(SparseArray<InstagramMediaModel> data){
        List<InstagramMediaModel> modelList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            InstagramMediaModel rawData = data.get(
                    data.keyAt(i));
            modelList.add(rawData);
        }
        return modelList;
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this,listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this,listPermission);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GALLERY_BROWSER;
    }
}
