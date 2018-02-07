package com.tokopedia.core.newgallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.fragment.ImageGalleryAlbumFragment;
import com.tokopedia.core.myproduct.fragment.ImageGalleryFragment;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.presenter.ImageGallery;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.presenter.ImageGalleryImpl;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

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
    public static final String COMPRESS_TO_TKPD = "CMPRS_TKPD";

    public static final int ADD_PRODUCT_IMAGE_LOCATION_DEFAULT = 0;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int INSTAGRAM_SELECT_REQUEST_CODE = 200;

    public static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";
    public static final String PRODUCT_SOC_MED_DATA = "PRODUCT_SOC_MED_DATA";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_URLS = "image_urls";
    public static final String IMAGE_PATH_CAMERA = "IMAGE_PATH_CAMERA";
    public static final String IS_CAMERA_OPEN = "IS_CAMERA_OPEN";
    public static final String TOKOPEDIA = "Tokopedia";

    public static final int RESULT_CODE = 323;

    String FRAGMENT;
    int position;

    ImageGallery imageGalleryPresenter;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private FragmentManager supportFragmentManager;
    private Unbinder unbinder;

    private boolean forceOpenCamera;
    private int maxSelection;
    private Fragment galeryActivityFragment;
    private String imagePathCamera;
    private boolean isCameraOpen = false;

    private TkpdProgressDialog progressDialog;
    private boolean compressToTkpd;
    private boolean isFirstTime;

    /**
     * Call this to get image from image gallery
     *
     * @param context non null object
     */
    public static void moveToImageGallery(AppCompatActivity context, int position) {
        moveToImageGalleryCamera(context, position, false, -1, false);
    }

    /**
     * Call this to get image from image gallery and can select more that one.
     * alias to function moveToImageGalleryCamera
     *
     * @param context non null object
     */
    public static void moveToImageGallery(AppCompatActivity context, int position, int maxSelection) {
        moveToImageGalleryCamera(context, position, false, maxSelection, false);
    }

    public static void moveToImageGallery(Context context, android.app.Fragment fragment, int position, int maxSelection) {
        moveToImageGalleryCamera(context, fragment, position, false, maxSelection, false);
    }

    public static void moveToImageGallery(Context context, Fragment fragment, int position, int maxSelection) {
        moveToImageGalleryCamera(context, fragment, position, false, maxSelection, false);
    }

    public static void moveToImageGallery(AppCompatActivity context, int position, int maxSelection, boolean compressToTkpd) {
        moveToImageGalleryCamera(context, position, false, maxSelection, compressToTkpd);
    }

    public static void moveToImageGallery(Context context, android.app.Fragment fragment, int position, int maxSelection, boolean compressToTkpd) {
        moveToImageGalleryCamera(context, fragment, position, false, maxSelection, compressToTkpd);
    }

    public static void moveToImageGallery(Context context, Fragment fragment, int position, int maxSelection, boolean compressToTkpd) {
        moveToImageGalleryCamera(context, fragment, position, false, maxSelection, compressToTkpd);
    }

    /**
     * Call this to get image from image gallery
     * and force open camera.
     * without compressToTkpd
     */
    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera,
                                                int maxImageSelection) {
        moveToImageGalleryCamera(context, position, forceOpenCamera, maxImageSelection, false);
    }

    public static void moveToImageGalleryCamera(Context context, android.app.Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection) {
        moveToImageGalleryCamera(context, fragment, position, forceOpenCamera, maxImageSelection, false);
    }

    public static void moveToImageGalleryCamera(Context context, Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection) {
        moveToImageGalleryCamera(context, fragment, position, forceOpenCamera, maxImageSelection, false);
    }

    /**
     * Call this to get image from image gallery
     * and force open camera.
     * with compressToTkpd parameter
     *
     * @param compressToTkpd set true, will compress move the image to tkpd path after the images are selected.
     */

    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera,
                                                int maxImageSelection,
                                                boolean compressToTkpd) {
        Intent imageGallery = createIntent(context, position, forceOpenCamera, maxImageSelection, compressToTkpd);
        context.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    public static void moveToImageGalleryCamera(Context context, android.app.Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection,
                                                boolean compressToTkpd) {
        Intent imageGallery = createIntent(context, position, forceOpenCamera, maxImageSelection, compressToTkpd);
        fragment.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    public static void moveToImageGalleryCamera(Context context, Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection,
                                                boolean compressToTkpd) {
        Intent imageGallery = createIntent(context, position, forceOpenCamera, maxImageSelection, compressToTkpd);
        fragment.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    protected static Intent createIntent(Context context, int position,
                                       boolean forceOpenCamera,
                                       int maxImageSelection,
                                       boolean compressToTkpd) {
        Intent imageGallery = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, ImageGalleryAlbumFragment.FRAGMENT_TAG);
        bundle.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
        bundle.putBoolean(FORCE_OPEN_CAMERA, forceOpenCamera);
        bundle.putInt(MAX_IMAGE_SELECTION, maxImageSelection);
        bundle.putBoolean(COMPRESS_TO_TKPD, compressToTkpd);
        imageGallery.putExtras(bundle);
        return imageGallery;
    }

    /**
     * function to generate file when capture image from CAMERA
     */
    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + TOKOPEDIA + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + System.currentTimeMillis() / 1000L + ".jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstTime = savedInstanceState == null;

        onRestoreSavedState(savedInstanceState);

        fetchExtras(getIntent());
        setContentView(R.layout.activity_gallery);
        unbinder = ButterKnife.bind(this);

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


        imageGalleryPresenter = new ImageGalleryImpl(this);

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
        if (imageGalleryPresenter != null) {
            imageGalleryPresenter.getItemAlbum();
        }
    }

    @Override
    public void fetchImageFromDb(String folderPath) {
        if (imageGalleryPresenter != null) {
            imageGalleryPresenter.getItemListAlbum(folderPath);
        }
    }

    @Override
    public void loadData(List<FolderModel> models) {
       /* do nothing */
    }

    @Override
    public void retrieveData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> dataAlbum) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageGalleryAlbumFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryAlbumFragment) {
            ImageGalleryAlbumFragment imageGalleryAlbumFragment = (ImageGalleryAlbumFragment) fragment;
            // Load Data
            imageGalleryAlbumFragment.addDatas(dataAlbum);
        }
    }

    @Override
    public void retrieveItemData(ArrayList<com.tokopedia.core.newgallery.model.ImageModel> data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment) {
            ((ImageGalleryFragment) fragment).addItems(data);
        }
    }

    @Override
    public boolean isNeedPermission() {
        return PermissionUtils.hasSelfPermissions(this, new String[] {"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE"});
    }

    @Override
    public void moveToGallery(List<ImageModel> imageModels, int maxSelection) {
        /* do nothing removed this later */
    }

    @Override
    public void moveToGallery(int position, int maxSelection) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment) {
            Log.d(TAG, messageTAG + ImageGalleryFragment.FRAGMENT_TAG + " already created!!!");
        } else {
            Fragment imageGalleryFragment = ImageGalleryFragment.newInstance(imageGalleryPresenter.getFolderPath(position), maxSelection);
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
            Bundle bundle = intent.getExtras();

            position = bundle.getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            forceOpenCamera = bundle.getBoolean(FORCE_OPEN_CAMERA, false);
            maxSelection = bundle.getInt(MAX_IMAGE_SELECTION, -1);
            compressToTkpd = bundle.getBoolean(COMPRESS_TO_TKPD, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SessionHandler.isFirstTimeAskedPermissionStorage(GalleryActivity.this)
                || (Build.VERSION.SDK_INT >= 23
                && shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)))
            GalleryActivityPermissionsDispatcher.initContentWithCheck(this);
        else
            RequestPermissionUtil.onFinishActivityIfNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void initContent() {
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);

        if (forceOpenCamera ) {
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
            if (compressToTkpd) {
                File photo = FileUtils.writeImageToTkpdPath(path);
                if (photo != null) {
                    finishWithSingleImage(photo.getAbsolutePath());
                }
            } else {
                finishWithSingleImage(path);
            }
        }
    }

    @Override
    public void sendResultImageGallery(List<String> paths) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(ImageGalleryFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof ImageGalleryFragment && checkCollectionNotNull(paths)) {
            Intent intent = new Intent();
            if (compressToTkpd) {
                ArrayList<String> tkpdPaths = new ArrayList<>();
                for (int i = 0, sizei = paths.size(); i < sizei; i++) {
                    String path = paths.get(i);
                    File photo = FileUtils.writeImageToTkpdPath(path);
                    if (photo != null) {
                        tkpdPaths.add(photo.getAbsolutePath());

                        FileUtils.deleteAllCacheTkpdFile(path);
                    }
                }
                if (tkpdPaths.size() > 0) {
                    finishWithMultipleImage(new ArrayList<>(paths));
                }
            } else {
                finishWithMultipleImage(new ArrayList<>(paths));
            }
        }
    }

    @Override
    public ActionMode showActionMode(ActionMode.Callback callback) {
        if (checkNotNull(callback))
            return startSupportActionMode(callback);
        else
            return null;
    }

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

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onInstagramClicked() {
        if (getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getApplication()).startInstopedActivityForResult(GalleryActivity.this,
                    INSTAGRAM_SELECT_REQUEST_CODE, maxSelection);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onFabClicked() {
        switch (FRAGMENT) {
            case ImageGalleryAlbumFragment.FRAGMENT_TAG:
            case ImageGalleryFragment.FRAGMENT_TAG:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_REMOVED)) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File outputMediaFile = getOutputMediaFile();
                        if (outputMediaFile != null && !isCameraOpen) {
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
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    forceOpenCamera = false;
                    finish();
                    break;
                case RESULT_OK:
                    switch (FRAGMENT) {
                        case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                        case ImageGalleryFragment.FRAGMENT_TAG:
                            if (imagePathCamera != null) {
                                if (compressToTkpd) {
                                    File photo = FileUtils.writeImageToTkpdPath(imagePathCamera);
                                    if (photo != null) {
                                        FileUtils.deleteAllCacheTkpdFile(imagePathCamera);
                                        finishWithSingleImage(photo.getAbsolutePath());
                                    } else {
                                        finishWithSingleImage(imagePathCamera);
                                    }
                                } else {
                                    finishWithSingleImage(imagePathCamera);
                                }
                            }
                            break;
                    }
                    break;
            }
        } else if (requestCode == INSTAGRAM_SELECT_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    List<InstagramMediaModel> images = data.getParcelableArrayListExtra(PRODUCT_SOC_MED_DATA);

                    ArrayList<String> standardResoImageUrlList = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        standardResoImageUrlList.add(images.get(i).standardResolution);
                    }
                    showProgressDialog();
                    ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(this);
                    imageDownloadHelper.convertHttpPathToLocalPath(standardResoImageUrlList, false,
                            new ImageDownloadHelper.OnImageDownloadListener() {
                                @Override
                                public void onError(Throwable e) {
                                    hideProgressDialog();
                                    CommonUtils.UniversalToast(GalleryActivity.this,
                                            ErrorHandler.getErrorMessage(e));
                                }

                                @Override
                                public void onSuccess(ArrayList<String> resultLocalPaths) {
                                    hideProgressDialog();
                                    finishWithMultipleImage(resultLocalPaths);
                                }
                            });
                    break;
                default:
                    // no op
                    break;
            }
        }
    }

    // will be overriden in ImageEditorActivity
    public void finishWithSingleImage(String imageUrl){
        Intent intent = new Intent();
        intent.putExtra(GalleryActivity.IMAGE_URL, imageUrl);
        intent.putExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, position);
        setResult(GalleryActivity.RESULT_CODE, intent);
        finish();
    }

    public void finishWithMultipleImage(ArrayList<String> imageUrls){
        Intent intent = new Intent();
        intent.putStringArrayListExtra(GalleryActivity.IMAGE_URLS, imageUrls);
        if (imageUrls!= null && imageUrls.size() == 1) {
            intent.putExtra(GalleryActivity.IMAGE_URL, imageUrls.get(0));
        }
        intent.putExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, position);
        setResult(GalleryActivity.RESULT_CODE, intent);
        finish();
    }

    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (! progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog(){
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onPermissionDenied(this, listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onNeverAskAgain(this, listPermission);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GALLERY_BROWSER;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        imageGalleryPresenter.detach();
    }
}