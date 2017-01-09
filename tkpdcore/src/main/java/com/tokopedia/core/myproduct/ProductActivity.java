package com.tokopedia.core.myproduct;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sromku.simple.fb.SimpleFacebook;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.fragment.TwitterDialogV4;
import com.tokopedia.core.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.fragment.ChooserDialogFragment;
import com.tokopedia.core.myproduct.fragment.ChooserFragment;
import com.tokopedia.core.myproduct.fragment.ImageChooserDialog;
import com.tokopedia.core.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.presenter.AddProductView;
import com.tokopedia.core.myproduct.presenter.ProductView;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.myproduct.service.ProductServiceConstant;
import com.tokopedia.core.myproduct.utils.AddProductType;
import com.tokopedia.core.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by sebastianusk on 03/01/2017.
 */
public class ProductActivity extends BaseProductActivity implements
        ProductView,
        ChooserFragment.OnListFragmentInteractionListener,
        DownloadResultReceiver.Receiver,
        DownloadResultSender,
        TwitterDialogV4.TwitterInterface,
        DialogFragmentImageAddProduct.DFIAListener,
        ImageChooserDialog.SelectWithImage
{

    private static final String TAG = "ProductActivity";
    public static final String MAX_IMAGE_SELECTION = "MAX_IMAGE_SELECTION";
    SimpleFacebook simplefacebook;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.add_product_container)
    FrameLayout container;

    FloatingActionButton fab;

    String FRAGMENT;
    int position;
    String imagePathFromImport;
    String[] multiImagePathFromImport;
    boolean isEdit;
    boolean isCopy;
    boolean isModify;
    String productId;
    private long productDb;

    DownloadResultReceiver mReceiver;

    FragmentManager supportFragmentManager;
    public static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";

    public static final String ADD_PRODUCT_IMAGE_LOCATION = "ADD_PRODUCT_IMAGE_LOCATION";
    public static final int ADD_PRODUCT_IMAGE_LOCATION_DEFAULT = 0;


    // currently supported type
    public static final int ADD_PRODUCT_CATEGORY = 0;
    public static final int ADD_PRODUCT_CHOOSE_ETALASE = 1;


    // fragment productActifity, moved there because it is needed for twitter dialog
    Fragment productActifityFragment = null;
    private Unbinder unbinder;
    private String messageTAG = "Product";

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

        /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
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

    public static String getPath(Context context, Uri contentUri) {

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

    public static String getRealPathFromURI(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return getPath(inContext, Uri.parse(path));
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

        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);
    }

    public void fetchSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            FRAGMENT = bundle.getString(FRAGMENT_TO_SHOW);
            position = bundle.getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
        }
    }

    public void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which fragment should be created
            String fragment = intent.getExtras().getString(FRAGMENT_TO_SHOW);
            if (fragment != null) {
                switch (fragment) {
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

            isEdit = intent.getExtras().getBoolean("is_edit", false);
            isCopy = intent.getExtras().getBoolean("is_copy", false);
            isModify = intent.getExtras().getBoolean("is_modify", false);
            productId = intent.getExtras().getString("product_id", "XXX");
            productDb = intent.getExtras().getLong("product_db", -1);

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

    public void initFragment(String FRAGMENT_TAG) {

        switch (FRAGMENT_TAG) {
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
            case AddProductFragment.FRAGMENT_EDIT_TAG:
                throw new RuntimeException("not implemented yet");
        }
    }

    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.add_product_container, fragment, TAG);
        if (isAddtoBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        }

        simplefacebook.onActivityResult(requestCode, resultCode, data);
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
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ProductService.TYPE, ProductService.INVALID_TYPE);
        Fragment fragment = null;
        switch (type) {
            case TkpdState.AddProduct.EDIT_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.AddProduct.DELETE_PRODUCT:
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
                        case TkpdState.AddProduct.ADD_PRODUCT:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
                        case TkpdState.AddProduct.ADD_PRODUCT_WITHOUT_IMAGE:
                        case TkpdState.AddProduct.EDIT_PRODUCT:
                        case TkpdState.AddProduct.DELETE_PRODUCT:
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
                        case TkpdState.AddProduct.ADD_PRODUCT:
                            break;
                        case TkpdState.AddProduct.ADD_PRODUCT_WITHOUT_IMAGE:
                            if (resultData.getBoolean(ProductService.RETRY_FLAG, false)) {
                                boolean retry = resultData.getBoolean(ProductService.RETRY_FLAG, false);
                                ((BaseView) fragment).ariseRetry(type, retry);
                            } else {
                                ((BaseView) fragment).setData(type, resultData);
                            }
                        case TkpdState.AddProduct.DELETE_PRODUCT:
                        case TkpdState.AddProduct.EDIT_PRODUCT:
                            if (resultData.getBoolean(ProductService.RETRY_FLAG, false)) {
                                boolean retry = resultData.getBoolean(ProductService.RETRY_FLAG, false);
                                ((BaseView) fragment).ariseRetry(type, retry);
                            } else {
                                CacheInteractor cacheInteractor = new CacheInteractorImpl();
                                cacheInteractor.deleteProductDetail(resultData.getInt(ProductService.PRODUCT_ID));
                                ((BaseView) fragment).setData(type, resultData);
                            }
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
            case TkpdState.AddProduct.EDIT_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT:
            case TkpdState.AddProduct.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.AddProduct.DELETE_PRODUCT:
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
                    GalleryActivity.moveToImageGallery(this, position, emptyPicture);
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

    @Override
    public String getScreenName() {
        return "";
    }
}
