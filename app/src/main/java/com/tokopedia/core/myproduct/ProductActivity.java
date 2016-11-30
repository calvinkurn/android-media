package com.tokopedia.core.myproduct;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.sromku.simple.fb.SimpleFacebook;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.fragment.TwitterDialogV4;
import com.tokopedia.core.instoped.InstagramAuth;
import com.tokopedia.core.instoped.fragment.InstagramMediaFragment;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.fragment.ChooserDialogFragment;
import com.tokopedia.core.myproduct.fragment.ChooserFragment;
import com.tokopedia.core.myproduct.fragment.ImageChooserDialog;
import com.tokopedia.core.myproduct.fragment.ImageGalleryAlbumFragment;
import com.tokopedia.core.myproduct.fragment.ImageGalleryFragment;
import com.tokopedia.core.myproduct.fragment.ReturnPolicyDialog;
import com.tokopedia.core.myproduct.model.FolderModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.presenter.AddProductView;
import com.tokopedia.core.myproduct.presenter.ImageGallery;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.presenter.ImageGalleryView;
import com.tokopedia.core.myproduct.presenter.ProductSocMedPresenter;
import com.tokopedia.core.myproduct.presenter.ProductView;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.myproduct.service.ProductServiceConstant;
import com.tokopedia.core.myproduct.utils.AddProductType;
import com.tokopedia.core.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.io.File;
import java.io.InputStream;
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
import permissions.dispatcher.RuntimePermissions;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by m.normansyah on 03/12/2015.
 */
@RuntimePermissions
public class ProductActivity extends BaseProductActivity implements
        ImageGalleryView,
        ProductView,
        ChooserFragment.OnListFragmentInteractionListener,
        DownloadResultReceiver.Receiver,
        ReturnPolicyDialog.ReturnPolicyListener,
        DownloadResultSender,
        TwitterDialogV4.TwitterInterface,
        DialogFragmentImageAddProduct.DFIAListener,
        ImageChooserDialog.SelectWithImage
{

    public static final String FORCE_OPEN_CAMERA = "FORCE_OPEN_CAMERA";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    SimpleFacebook simplefacebook;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

//    @BindView(R2.id.app_bar_layout)
//    AppBarLayout appBarLayout;

    @BindView(R2.id.add_product_container)
    FrameLayout container;

    FloatingActionButton fab;

    ImageGallery imageGallery;

    String FRAGMENT;
    int position;
    String imagePathCamera;
    String imagePathFromImport;
    String[] multiImagePathFromImport;
    boolean forceOpenCamera;
    boolean isEdit;
    boolean isCopy;
    boolean isModify;
    String productId;
    private long productDb;
    int maxSelection;

    DownloadResultReceiver mReceiver;

    FragmentManager supportFragmentManager;
    public static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";

    public static final String ADD_PRODUCT_IMAGE_LOCATION = "ADD_PRODUCT_IMAGE_LOCATION";
    public static final String MAX_IMAGE_SELECTION = "MAX_IMAGE_SELECTION";
    public static final int ADD_PRODUCT_IMAGE_LOCATION_DEFAULT = 0;


    // currently supported type
    public static final int ADD_PRODUCT_CATEGORY = 0;
    public static final int ADD_PRODUCT_CHOOSE_ETALASE = 1;
    public static final int EDIT_PHOTO_DIALOG = 2;


    // fragment productActifity, moved there because it is needed for twitter dialog
    Fragment productActifityFragment = null;
    private Unbinder unbinder;

    public static File getOutputMediaFile(){
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + System.currentTimeMillis()/1000L + ".jpg");
        return mediaFile;
    }


    ImageChooserDialog imageChooserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getImplicitIntent();

        if (this.isFinishing()) {
            return;
        }
        simplefacebook = SimpleFacebook.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        fetchSaveInstanceState(savedInstanceState);
        fetchExtras(getIntent());
        switch (FRAGMENT) {
            case AddProductFragment.FRAGMENT_TAG:
                setContentView(R.layout.activity_product2);
                break;
            default:
                setContentView(R.layout.activity_product);
                break;
        }
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


        imageGallery = new ImageGalleryImpl(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductActivityPermissionsDispatcher.onFabClickedWithCheck(ProductActivity.this, view);
                }
            });
        if (FRAGMENT.equals(InstagramAuth.TAG)) {
            fab.setVisibility(View.GONE);
        }

         /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onFabClicked(View view) {
        switch (FRAGMENT) {
            case ImageGalleryAlbumFragment.FRAGMENT_TAG:
            case ImageGalleryFragment.FRAGMENT_TAG:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_REMOVED)) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File outputMediaFile = getOutputMediaFile();
                        imagePathCamera = outputMediaFile.getAbsolutePath();
                        Uri fileuri = Uri.fromFile(outputMediaFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
                        startActivityForResult(takePictureIntent,
                                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                } else {
                    WarningDialog();
                }
                break;
        }
    }

    private void getImplicitIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (SessionHandler.isV4Login(this)) {
                if (!SessionHandler.getShopID(this).equals("0")) {
                    if (type.startsWith("image/")) {
                        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        processSingleImage(intent, imageUri);
                    }
                } else {
                    CommonUtils.UniversalToast(getBaseContext(),
                            getString(R.string.title_no_shop));

                    finish();
                }
            } else {
                Intent intentLogin = SessionRouter.getLoginActivityIntent(this);
                intentLogin.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                startActivity(intentLogin);
                finish();
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (SessionHandler.isV4Login(this)) {
                if (!SessionHandler.getShopID(this).equals("0")) {
                    if (type.startsWith("image/")) {
                        ArrayList<Uri> imageUris = intent
                                .getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                        if (checkCollectionNotNull(imageUris)) {
                            processMultipleImage(imageUris);
                        }

                        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        processSingleImage(intent, imageUri);
                    }
                } else {
                    finish();
                    CommonUtils.UniversalToast(getBaseContext(),
                            getString(R.string.title_no_shop));
                }

            } else {
                Intent intentLogin = SessionRouter.getLoginActivityIntent(this);
                intentLogin.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                startActivity(intentLogin);
                finish();
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    public void triggerAppBarAnimation(boolean turnedOn){
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if(turnedOn) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            params.setScrollFlags(0);
        }
        toolbar.setLayoutParams(params);
    }

    private void processMultipleImage(ArrayList<Uri> imageUris) {
        int imagescount = (imageUris.size() > 5) ? 5 : imageUris.size();
        multiImagePathFromImport = new String[imagescount];
        for (int i = 0; i < imagescount; i++) {
            Uri imageUri = imageUris.get(i);

            FRAGMENT = AddProductFragment.FRAGMENT_TAG;


            if (imageUri.toString().startsWith("content://gmail-ls/")) {// get email attachment from gmail
                multiImagePathFromImport[i] = getPathFromGmail(imageUri);
            } else { // get extras for import from gallery
                multiImagePathFromImport[i] = getRealPathFromURI(this, imageUri);
            }
            Log.d(TAG, messageTAG + " [" + multiImagePathFromImport[i] + "]");
        }
    }

    private void processSingleImage(Intent intent, Uri imageUri) {
        if (imageUri != null) {
            FRAGMENT = AddProductFragment.FRAGMENT_TAG;

            position = intent.getExtras().getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);

            if (imageUri.toString().startsWith("content://gmail-ls/")) {
                imagePathFromImport = getPathFromGmail(imageUri);
            } else {
                // get extras for import from gallery
                imagePathFromImport = getRealPathFromURI(this, imageUri);
            }
            Log.d(TAG, messageTAG + " [" + imagePathFromImport + "]");
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {

        String res = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } else {
            Log.d(TAG, "Cursor is null");
            return contentUri.getPath();
        }
        return res;
    }

    public String getPathFromGmail(Uri contentUri) {
        File attach = null;
        try {
            InputStream attachment = getContentResolver().openInputStream(contentUri);
            attach = UploadPhotoTask.writeImageToTkpdPath(attachment);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return attach.getAbsolutePath();
    }

    @Override
    public void WarningDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.dialog_no_memory_card));
        myAlertDialog.setPositiveButton(getString(R.string.title_ok), null);

        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void showTwitterDialog() {
        // Create and show the dialog.
        TwitterDialogV4 newFragment = new TwitterDialogV4();
        newFragment.show(supportFragmentManager, TWITTER_DIALOG_V_4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SessionHandler.isFirstTimeAskedPermissionStorage(ProductActivity.this)
                || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            ProductActivityPermissionsDispatcher.checkPermissionWithCheck(this);
        else
            RequestPermissionUtil.onFinishActivityIfNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        simplefacebook = SimpleFacebook.getInstance(this);
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);

        if (forceOpenCamera && checkNotNull(fab)) {
            fab.performClick();
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void checkPermission() {
        CommonUtils.dumper("NISNISNIS ProductActivity Storage");
    }

    public void fetchSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            FRAGMENT = bundle.getString(FRAGMENT_TO_SHOW);
            position = bundle.getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
        }
    }

    @Override
    public void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which fragment should be created
            String fragment = intent.getExtras().getString(FRAGMENT_TO_SHOW);
            if (fragment != null) {
                switch (fragment) {
                    case InstagramAuth.TAG:
                    case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                    case ImageGalleryFragment.FRAGMENT_TAG:
                    case AddProductFragment.FRAGMENT_TAG:
                        FRAGMENT = fragment;
                        break;
                }
            } else {
                FRAGMENT = AddProductFragment.FRAGMENT_TAG;
            }

            position = intent.getExtras().getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);

            // get extras for import from gallery
            if (!checkNotNull(imagePathFromImport))
                imagePathFromImport = intent.getExtras().getString(ManageProduct.IMAGE_GALLERY);

            ArrayList<String> imagePaths = Parcels.unwrap(intent.getExtras().getParcelable(GalleryBrowser.IMAGE_URLS));
            if (checkNotNull(imagePaths) && position == -1) {
                multiImagePathFromImport = new String[imagePaths.size()];
                int count = 0;
                for (String imagePath : imagePaths) {
                    multiImagePathFromImport[count] = imagePath;
                    count++;
                }
            }

            forceOpenCamera = intent.getExtras().getBoolean(FORCE_OPEN_CAMERA, false);

            isEdit = intent.getExtras().getBoolean("is_edit", false);
            isCopy = intent.getExtras().getBoolean("is_copy", false);
            isModify = intent.getExtras().getBoolean("is_modify", false);
            productId = intent.getExtras().getString("product_id", "XXX");
            productDb = intent.getExtras().getLong("product_db", -1);
            maxSelection = intent.getExtras().getInt(MAX_IMAGE_SELECTION, -1);

            int notificationId = intent.getIntExtra(ProductService.NOTIFICATION_ID,-1);
            if (notificationId != -1){
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(notificationId);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
    }

    @Override
    public void initFragment(String FRAGMENT_TAG) {

        switch (FRAGMENT_TAG) {
            case ImageGalleryAlbumFragment.FRAGMENT_TAG:
                if (supportFragmentManager.findFragmentByTag(ImageGalleryAlbumFragment.FRAGMENT_TAG) == null) {
                    if (maxSelection == -1)
                        productActifityFragment = ImageGalleryAlbumFragment.newInstance();
                    else {
                        productActifityFragment = ImageGalleryAlbumFragment.newInstance(maxSelection);
                    }
                    moveToFragment(productActifityFragment, true, ImageGalleryAlbumFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + ImageGalleryAlbumFragment.FRAGMENT_TAG + " is already created");
                }
                break;
            case ImageGalleryFragment.FRAGMENT_TAG:
                Log.d(TAG, messageTAG + ImageGalleryFragment.FRAGMENT_TAG + " can be accessed from " + ImageGalleryAlbumFragment.FRAGMENT_TAG);
                break;
            case AddProductFragment.FRAGMENT_TAG:
                if (supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG) == null) {
                    if (imagePathFromImport != null && position != -1) {
                        Log.d(TAG, messageTAG + " tambahkan dari gallery [" + imagePathFromImport + "] position [" + position + "]");
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD_FROM_GALLERY.getType(), imagePathFromImport, position);
                    } else if (multiImagePathFromImport != null) {
                        Log.d(TAG, messageTAG + " tambahkan multiple dari gallery [" + multiImagePathFromImport + "]");
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD_MULTIPLE_FROM_GALERY.getType(), multiImagePathFromImport);
                    } else if (isEdit && !productId.equals("XXX")) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.EDIT.getType(), productId);
                    } else if (isCopy && !productId.equals("XXX")) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.COPY.getType(), productId);
                    }else if(isModify && productDb != -1) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.MODIFY.getType(), productDb);
                    }else {
                        // test for add first time
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD.getType());
                    }
                    // test for add using social media
//                    fragment = AddProductFragment.newInstance(AddProductType.ADD_FROM_SOCIAL_MEDIA.getType());
                    moveToFragment(productActifityFragment, true, AddProductFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + AddProductFragment.FRAGMENT_TAG + " is already created");
                }
                break;
            case InstagramAuth.TAG:
                InstagramAuth auth = new InstagramAuth();
                auth.getMedias(supportFragmentManager);
                break;
            case AddProductFragment.FRAGMENT_EDIT_TAG:
                throw new RuntimeException("not implemented yet");
        }
    }

    public InstagramMediaFragment.OnGetInstagramMediaListener onGetInstagramMediaListener() {
        return new InstagramMediaFragment.OnGetInstagramMediaListener() {
            @Override
            public void onSuccess(SparseArray<InstagramMediaModel> selectedModel) {
                selectedModel.size();
                //[START] move to productSocMedActivity
                Intent moveToProductSocMed = new Intent(ProductActivity.this, ProductSocMedActivity.class);
                moveToProductSocMed.putExtra(
                        ProductSocMedPresenter.PRODUCT_SOC_MED_DATA,
                        Parcels.wrap(selectedModel)
                );
                ProductActivity.this.startActivity(moveToProductSocMed);
                ProductActivity.this.finish();
                //[END] move to productSocMedActivity
            }
        };
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
            return null;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.home:
                Log.d(TAG, messageTAG + " R.id.home !!!");
                return true;
            case android.R.id.home:
                Log.d(TAG, messageTAG + " android.R.id.home !!!");
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void fetchImageFromDb() {
        if (imageGallery != null) {
            imageGallery.fetchImageUsingDb(this);
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

    public static void moveToAddProduct(Context context) {
        if (!checkNotNull(context))
            return;

        Intent intent = new Intent(context, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void showPopup(int type, String title, List<SimpleTextModel> simpleTextModels) {
        showPopup(getSupportFragmentManager(), type, title, simpleTextModels);
    }

    public static void moveToProductShare(ShareData shareData, Context context) {

        context.startActivity(ProductInfoActivity.createInstance(context, shareData));

        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }

    public static void moveToProductShare(Bundle bundle, Context context) {

        context.startActivity(ProductInfoActivity.createInstance(context, bundle));

        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }

    public static void showPopup(FragmentManager fm, int type
            , String title, List<SimpleTextModel> simpleTextModels) {
        DialogFragment dialogFragment;
        switch (type) {
            case ADD_PRODUCT_CATEGORY:
            case ADD_PRODUCT_CHOOSE_ETALASE:
                dialogFragment = ChooserDialogFragment.newInstance(type, title, simpleTextModels);
                dialogFragment.show(fm, ChooserDialogFragment.FRAGMENT_TAG);
                break;
        }
    }

    @Deprecated
    public void showPopup(String title, List<SimpleTextModel> simpleTextModels) {
        DialogFragment dialogFragment = ChooserDialogFragment.newInstance(title, simpleTextModels);
        dialogFragment.show(supportFragmentManager, ChooserDialogFragment.FRAGMENT_TAG);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(IMAGE_URL);
            Log.d(TAG, messageTAG + imageUrl + " & " + position);
            Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
            if (fragment != null && fragment instanceof AddProductFragment && checkNotNull(imageUrl)) {
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrl, position);
            }

            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryBrowser.IMAGE_URLS);
            if(fragment != null && fragment instanceof AddProductFragment && checkCollectionNotNull(imageUrls)){
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrls.get(0), position);
                imageUrls.remove(0);
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrls);
            }
        }else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
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

        simplefacebook.onActivityResult(requestCode, resultCode, data);
    }

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

    public static Intent moveToEditFragment(Context context, boolean isEdit, String productId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_edit", true);
        bundle.putString("product_id", productId);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    public static Intent moveToCopyFragment(Context context, boolean isCopy, String productId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_copy", true);
        bundle.putString("product_id", productId);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    public static Intent moveToModifyProduct(Context context, long productDb){
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_modify", true);
        bundle.putLong("product_db", productDb);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    /**
     * Call this to get image from image gallery
     * and force open camera.
     *
     * @param context non null object
     */
    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera, int maxImageSelection){
        Intent imageGallery = new Intent(context, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, ImageGalleryAlbumFragment.FRAGMENT_TAG);
        bundle.putBoolean(FORCE_OPEN_CAMERA, forceOpenCamera);
        bundle.putInt(MAX_IMAGE_SELECTION, maxImageSelection);
        imageGallery.putExtras(bundle);

        //[START] This one is old one
//        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        //[END] This one is old one
        context.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    @Override
    public void onListFragmentInteraction(SimpleTextModel item) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        Fragment dialog = supportFragmentManager.findFragmentByTag(ChooserDialogFragment.FRAGMENT_TAG);
        switch (((ChooserDialogFragment) dialog).getType()) {
            case ADD_PRODUCT_CATEGORY:
                if (fragment != null && fragment instanceof AddProductFragment) {
                    //[START] This is old using json reading
//                    ((AddProductFragment) fragment).addCategoryAfterSelect(item);
                    //[END] This is old using json reading

                    ((AddProductFragment) fragment).addCategoryAfterSelectV2(item);
                }
                break;
            case ADD_PRODUCT_CHOOSE_ETALASE:
                if (fragment != null && fragment instanceof AddProductFragment) {
                    ((AddProductFragment) fragment).addEtalaseAfterSelect(item);
                }
                break;
        }
        if (dialog != null) {
            ((ChooserDialogFragment) dialog).dismiss();
        }

    }

    @Override
    public void onLongClick() {
        Log.e(TAG, "onLongClick not implemented yet");
//        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void refreshReturnPolicy() {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof AddProductFragment) {
            ((AddProductView) fragment).checkAvailibilityOfShopNote();
        }
    }

    @Override
    public void refreshReturnPolicy(String noteId) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof AddProductFragment) {
            ((AddProductView) fragment).checkAvailibilityOfShopNote();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ProductService.TYPE, ProductService.INVALID_TYPE);
        Fragment fragment = null;
        switch (type) {
            case ProductService.EDIT_PRODUCT:
            case ProductService.ADD_PRODUCT:
            case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case ProductService.DELETE_PRODUCT:
            case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
            case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if (fragment != null && fragment instanceof BaseView && type != ProductService.INVALID_TYPE) {
            switch (resultCode) {
                case ProductService.STATUS_RUNNING:
                    switch (type) {
                        case ProductService.ADD_PRODUCT:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
                        case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                        case ProductService.EDIT_PRODUCT:
                        case ProductService.DELETE_PRODUCT:
                            //[START] show progress bar
                            if (fragment instanceof AddProductFragment) {
//                                boolean showDialog = resultData.getBoolean(ProductService.ADD_PRODUCT_SHOW_DIALOG, false);
                                ((AddProductView) fragment).showProgress(true);
                            }
                            break;
                    }
                    break;
                case ProductService.STATUS_FINISHED:
                    switch (type) {
                        case ProductService.ADD_PRODUCT:
                            break;
                        case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                            if (resultData.getBoolean(ProductService.RETRY_FLAG, false)) {
                                boolean retry = resultData.getBoolean(ProductService.RETRY_FLAG, false);
                                ((BaseView) fragment).ariseRetry(type, retry);
                            } else {
                                ((BaseView) fragment).setData(type, resultData);
                            }
                        case ProductService.DELETE_PRODUCT:
                        case ProductService.EDIT_PRODUCT:
                            if (resultData.getBoolean(ProductService.RETRY_FLAG, false)) {
                                boolean retry = resultData.getBoolean(ProductService.RETRY_FLAG, false);
                                ((BaseView) fragment).ariseRetry(type, retry);
                            } else {
                                CacheInteractor cacheInteractor = new CacheInteractorImpl();
                                cacheInteractor.deleteProductDetail(resultData.getInt(ProductService.PRODUCT_ID));
                                ((BaseView) fragment).setData(type, resultData);
                            }
                            break;
                        case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
                            NoteDetailModel.Detail returnableNoteContent = Parcels.unwrap(resultData.getParcelable(ProductService.RETURNABLE_NOTE_CONTENT));
                            ((AddProductFragment) fragment).setProductReturnable(true);
                            ((AddProductFragment) fragment).saveReturnPolicyDetail(returnableNoteContent);
                            ((AddProductFragment) fragment).dismissReturnableDialog();
                            break;
                        case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                            ((AddProductFragment) fragment).clearAvailibilityOfShopNote();
                            ((AddProductFragment) fragment).checkAvailibilityOfShopNote();
                            returnableNoteContent = Parcels.unwrap(resultData.getParcelable(ProductService.RETURNABLE_NOTE_CONTENT));
                            ((AddProductFragment) fragment).setProductReturnable(true);
                            ((AddProductFragment) fragment).saveReturnPolicyDetail(returnableNoteContent);
                            ((AddProductFragment) fragment).dismissReturnableDialog();
                            break;
                    }
                    break;
                case ProductService.STATUS_ERROR:
                    switch (resultData.getInt(ProductService.NETWORK_ERROR_FLAG, ProductService.INVALID_NETWORK_ERROR_FLAG)) {
                        case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                            break;
                        case NetworkConfig.INTERNAL_SERVER_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                            break;
                        case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                            break;
                        case ProductService.INVALID_NETWORK_ERROR_FLAG:
                        default:
                            String messageError = resultData.getString(ProductService.MESSAGE_ERROR_FLAG, ProductService.INVALID_MESSAGE_ERROR);
                            if (!messageError.equals(ProductService.INVALID_MESSAGE_ERROR)) {
                                ((BaseView) fragment).onMessageError(type, messageError);
                            }

                    }
                    break;
            }// end of status download service
        }
    }

    @Override
    public void sendDataToInternet(int type, Bundle data) {
        switch (type) {
            case ProductService.EDIT_PRODUCT:
            case ProductService.ADD_PRODUCT:
            case ProductServiceConstant.ADD_PRODUCT_WITHOUT_IMAGE:
            case ProductService.DELETE_PRODUCT:
            case ProductService.UPDATE_RETURNABLE_NOTE_ADD_PRODUCT:
            case ProductService.ADD_RETURNABLE_NOTE_ADD_PRODUCT:
                ProductService.startDownload(this, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    public void ChangeUI() {
        if (supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG) != null) {
            ((AddProductFragment) supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG)).onLoginTwitter();
//            AddProductFragment.AddProductShare.authorizeTwitter();
        }
    }

    @Override
    public void editImage(int action, int position, int fragmentPosition, boolean isPrimary) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        switch (action) {
            case DialogFragmentImageAddProduct.DELETE_IMAGE:
                Log.d(TAG, "image delete : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>() {{
                        add(getString(R.string.error_delete_primary_image));
                    }});
                } else {
                    ((AddProductFragment) fragment).removeImageSelected(position);
                }
                break;
            case DialogFragmentImageAddProduct.CHANGE_IMAGE:
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>() {{
                        add(getString(R.string.error_change_primary_image));
                    }});
                } else {
                    int emptyPicture = 6 - ((AddProductFragment)fragment).countPicture();
                    Log.i(TAG, messageTAG + " max photo will get : " + emptyPicture);
                    ProductActivity.moveToImageGallery(this, position, emptyPicture);
                }
                break;
            case DialogFragmentImageAddProduct.ADD_DESCRIPTION:
                ((AddProductFragment) fragment).showImageDescDialog(position);
                break;
            case DialogFragmentImageAddProduct.CHANGE_TO_PRIMARY:
                ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                break;
            case DialogFragmentImageAddProduct.PRIMARY_IMAGE:
                Log.d(TAG, "image default : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
                    ((AddProductFragment) fragment).showImageDescDialog(position);
                } else {
                    ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void itemSelected(int index) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        if(fragment instanceof AddProductFragment){
            ((AddProductFragment)fragment).setProductCatalog(index);
        }
    }


    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() != 0) {
            //TODO: Perform your logic to pass back press here
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedListener) {
                    boolean canGoBack = ((OnBackPressedListener) fragment).onBackPressed();
                    if (!canGoBack) {
                        super.onBackPressed();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductActivityPermissionsDispatcher.onRequestPermissionsResult(ProductActivity.this, requestCode, grantResults);
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
}
