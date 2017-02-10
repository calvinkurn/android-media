package com.tokopedia.core.myproduct.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.ui.expandablelayout.ExpandableRelativeLayout;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.ClickToSelectEditText;
import com.tkpd.library.ui.widget.LimitedEditText;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.DownloadResultSender;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tkpd.library.utils.TwitterHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.database.model.CurrencyDB;
import com.tokopedia.core.database.model.CurrencyDB_Table;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.EtalaseDB_Table;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.PictureDB_Table;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.ProductDB_Table;
import com.tokopedia.core.database.model.WeightUnitDB;
import com.tokopedia.core.database.model.WeightUnitDB_Table;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.myproduct.ProductSocMedActivity;
import com.tokopedia.core.myproduct.adapter.ClickToSelectWithImage;
import com.tokopedia.core.myproduct.adapter.PhotoAdapter;
import com.tokopedia.core.myproduct.adapter.TextDeleteAdapter;
import com.tokopedia.core.myproduct.customview.wholesale.WholesaleLayout;
import com.tokopedia.core.myproduct.customview.wholesale.WholesaleModel;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.core.myproduct.model.EtalaseModel;
import com.tokopedia.core.myproduct.model.GetShopNoteModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.InputAddProductModel;
import com.tokopedia.core.myproduct.model.MyShopInfoModel;
import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.model.TextDeleteModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.core.myproduct.model.editProductForm.EditProductForm;
import com.tokopedia.core.myproduct.presenter.AddProductPresenter;
import com.tokopedia.core.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.core.myproduct.presenter.AddProductView;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.presenter.ImageGalleryView;
import com.tokopedia.core.myproduct.presenter.ProductSocMedPresenter;
import com.tokopedia.core.myproduct.presenter.ProductView;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.myproduct.utils.AddProductType;
import com.tokopedia.core.myproduct.utils.CurrencyFormatter;
import com.tokopedia.core.myproduct.utils.DelegateOnClick;
import com.tokopedia.core.myproduct.utils.MetadataUtil;
import com.tokopedia.core.myproduct.utils.PriceUtils;
import com.tokopedia.core.myproduct.utils.ProductEditHelper;
import com.tokopedia.core.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.myproduct.utils.VerificationUtils;
import com.tokopedia.core.myproduct.view.AddProductShare;
import com.tokopedia.core.myproduct.view.AddProductSocMedSubmit;
import com.tokopedia.core.myproduct.view.AddProductSubmit;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.ShareSocmedHandler;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;
import static com.tkpd.library.utils.CommonUtils.generateMessageError;
import static com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;

/**
 * Created by m.normansyah on 03/12/2015.
 * start support for multi fragment. 8/4/2016
 */
public class AddProductFragment extends TkpdBaseV4Fragment implements AddProductView, DelegateOnClick {

    public static final int DEFAULT_INVALID_VALUE = -999;
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String ADD_PRODUCT_IMAGE_LOCATION = "ADD_PRODUCT_IMAGE_LOCATION";
    public static final String ADD_PRODUCT_IMAGE_PATH = "ADD_PRODUCT_IMAGE_PATH";
    public static final String ADD_PROUCT_SOC_MED_RAW_DATA = "ADD_PROUCT_SOC_MED_RAW_DATA";
    public static final String STOCK_STATUS = "STOCK_STATUS";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DB = "product_db";
    public static final String ADD_PRODUCT_MULTIPLE_IMAGE_PATH = "ADD_PRODUCT_MULTIPLE_IMAGE_PATH";
    public static final String NO_CATALOG_OPTION = "Tidak menggunakan katalog";
    private Unbinder unbinder;

    @Override
    protected String getScreenName() {
        String screenName = null;
        switch (addProductType){
            case COPY:
                screenName = AppScreen.SCREEN_COPY_PRODUCT;
                break;
            case ADD_FROM_SOCIAL_MEDIA:
            case ADD:
            case ADD_MULTIPLE_FROM_GALERY:
            case EDIT:
            case MODIFY:
            case ADD_FROM_GALLERY:
            default:
                screenName = AppScreen.SCREEN_ADD_PRODUCT;
                break;
        }

        return screenName;
    }

    public void removeImageSelected(int i) {
        ImageModel imageModel = photos.get(i);
        if (imageModel.getPath() != null) {
            // if edit then dont delete the image product.
            if (addProductType == AddProductType.EDIT) {
                producteditHelper.deleteImage(imageModel, i);
            } else {
                DbManagerImpl.getInstance().removePictureWithId(imageModel.getDbId());
            }

            imageModel.setPath(null);
            imageModel.setResId(R.drawable.addproduct);
            imageModel.setDbId(0);
            photos.set(i, imageModel);

            setSelectedImageAsPrimary(0);
        } else {
            // do nothing
            Log.e(TAG, messageTAG + "failed removing images :" + (i + 1));
        }
    }
    //[END] this is deletion of images

    public void setSelectedImageAsPrimary(int i) {
        ImageModel imageModel = photos.get(i);
        if (getActivity() instanceof ProductSocMedActivity) {
            ((ProductSocMedActivity) getActivity()).changePicture(positionAtSocMed, imageModel);
        }
        List<ImageModel> newPhotos = new ArrayList<>();
        if (imageModel.getPath() != null) newPhotos.add(imageModel);
        for (int index = 0; index < photos.size(); index++) {
            imageModel = photos.get(index);
            if (i == index) {
                if (imageModel.getPath() != null) {
                    PictureDB pictureDB = DbManagerImpl.getInstance().getGambarById(imageModel.getDbId());
                    Log.i(TAG, "Picture before (new primary): " + pictureDB.toString());
                    if (pictureDB != null) {
                        pictureDB.setPicturePrimary(PictureDB.PRIMARY_IMAGE);
                        pictureDB.save();
                        Log.i(TAG, "Picture after (new primary): " + pictureDB.toString());
                    }
                }
                continue;
            }

            if (imageModel.getPath() != null) {
                PictureDB pictureDB = new Select().from(PictureDB.class).where(
                        PictureDB_Table.Id.is(imageModel.getDbId()))
                        .querySingle();
                Log.i(TAG, "Picture before (not primary): " + pictureDB.toString());
                if (pictureDB != null) {
                    pictureDB.setPicturePrimary(PictureDB.NOT_PRIMARY_IMAGE);
                    pictureDB.save();
                    Log.i(TAG, "Picture after (not primary): " + pictureDB.toString());
                }
                newPhotos.add(imageModel);
            }

        }
        for (int j = newPhotos.size(); j < 5; j++) {
            imageModel = new ImageModel();
            imageModel.setPath(null);
            imageModel.setResId(R.drawable.addproduct);
            imageModel.setDbId(0);
            photos.set(j, imageModel);
            newPhotos.add(imageModel);
        }

        initPhotoAdapter(newPhotos);
    }

    @BindView(R2.id.add_product_copy)
    ViewStub addProductCopy;

    @BindView(R2.id.add_product_submit_ll)
    ViewStub addProductSubmit;
    AddProductSubmit addProductSubmitContainer;

    @BindView(R2.id.add_product_soc_med_submit_ll)
    ViewStub addProductSocMedSubmit;
    AddProductSocMedSubmit addProductSocMedSubmitContainer;

    @BindView(R2.id.add_product_share)
    ViewStub addProductShare;
    AddProductShare addProductShareContainer;
    private AddProductPresenter addProduct;
    private ProductEditHelper producteditHelper = new ProductEditHelper();

    public void showImageDescDialog(int pos) {
        ImageModel imageModel = photos.get(pos);
        if (imageModel.getPath() != null) {
            // start dialog for description here
            PictureDB pictureDB = DbManagerImpl.getInstance().getGambarById(imageModel.getDbId());
            if (checkNotNull(pictureDB)) {// &&gambar.getPicturePrimary()==Gambar.PRIMARY_IMAGE
                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(imageModel.getDbId());
                fragment.show(getActivity().getSupportFragmentManager(), ImageDescriptionDialog.FRAGMENT_TAG);
            }
        } else {
            Log.e(TAG, messageTAG + "failed adding description for image");
        }
    }


    /**
     * upload to the ws just for this
     */
    public void pushProduct() {
        isCreateNewActivity = false;
        saveProduct(true);
    }

    /**
     * upload to ws then create new fragment
     */
    public void pushAndCreateNewProduct() {
        isCreateNewActivity = true;
        saveProduct(true);
    }

    /**
     * if single add product or edit product alone then let it to "0"
     * else if multiple add products then position should be vary
     */
    public int positionAtSocMed;

    GetShopNoteModel.ShopNoteModel returnPolicy = null;
    MyShopInfoModel.Info myShopInfoModel;
    NoteDetailModel.Detail detail;

    @BindView(R2.id.add_product_add_to_new_etalase_layout)
    ExpandableRelativeLayout addProductAddToNewEtalaseLayout;
    @BindView(R2.id.add_product_add_to_new_etalase_alert)
    TextInputLayout addProductAddToNewEtalaseAlert;
    @BindView(R2.id.add_product_add_to_new_etalase)
    EditText addProductAddToNewEtalase;

    @BindView(R2.id.add_product_imagechveron)
    ImageView chevron;

    @BindView(R2.id.add_product_minimum_order_alert)
    TextInputLayout addProductMinimumOrderAlert;
    @BindView(R2.id.add_product_minimum_order)
    EditText addProductMinimumOrder;

    @BindView(R2.id.add_product_product_name_alert)
    TextInputLayout addProductPRoductNameAlert;
    @BindView(R2.id.add_product_product_name)
    EditText addProductProductName;

    String selectedCurrencyDesc = "Rp";
    @BindView(R2.id.add_product_currency)
    ClickToSelectEditText<SimpleTextModel> addProductCurrency;
    @BindView(R2.id.add_product_price)
    EditText addProductPrice;
    @BindView(R2.id.add_product_price_alert)
    TextInputLayout addProductPriceAlert;

    RecyclerView.Adapter photoAdapter;
    @BindView(R2.id.add_product_images)
    RecyclerView addProductImages;
    ArrayList<ImageModel> photos;

    TextDeleteAdapter categoryAdapter;
    @BindView(R2.id.add_product_category_spinner)
    RecyclerView addProductCategorySpinner;
    ArrayList<TextDeleteModel> categoryModels;

    @BindView(R2.id.add_product_weight_unit)
    ClickToSelectEditText addProductWeightUnit;
    @BindView(R2.id.add_product_weight)
    EditText addProductWeight;
    @BindView(R2.id.add_product_weight_alert)
    TextInputLayout addProductWeightAlert;

    @BindView(R2.id.add_product_tittle_wholesale)
    RelativeLayout addProductTitleWholeSale;

    @BindView(R2.id.add_product_wholesale_container)
    ExpandableRelativeLayout wholeSaleContainer;

    @BindView(R2.id.add_product_wholesale_layout)
    WholesaleLayout wholesaleLayout;

    @BindView(R2.id.add_product_add_to)
    RecyclerView addProductAddTo;
    ArrayList<EtalaseModel> etalaseModels;
    ArrayList<TextDeleteModel> displayEtalaseModels;
    TextDeleteAdapter etalaseAdapter;

    @BindView(R2.id.add_product_parent)
    NestedScrollView addProductParent;


    @BindView(R2.id.add_product_desc)
    LimitedEditText addProductDesc;
    EditText addProductDescNormal;

    @BindView(R2.id.add_product_product_desc_layout)
    TextInputLayout addProductProductDescLayout;

    @BindView(R2.id.add_product_condition)
    Spinner addProductCondition;

    @BindView(R2.id.add_product_insurance)
    Spinner addProductInsurance;

    @BindView(R2.id.title_preorder)
    RelativeLayout addProductTitlePreorder;
    @BindView(R2.id.chevron_preorder)
    ImageView addProductChevronPreorder;
    @BindView(R2.id.preorder_content)
    ExpandableRelativeLayout addProductPreOderContent;
    @BindView(R2.id.edittext_preorder)
    EditText addProductEdittextPreorder;

    @BindView(R2.id.add_product_catalog_layout)
    LinearLayout addProductCatalogLayout;
    @BindView(R2.id.add_product_prompt_catalog)
    TextView addProductPromptCatalog;
    @BindView(R2.id.add_product_catalog)
    ClickToSelectWithImage addProductCatalog;
    ArrayList<CatalogDataModel.Catalog> catalogs;

    TkpdProgressDialog tkpdProgressDialog;

    View parentView;

    ProductDB ProductDb;

    KeyboardHandler keyboardHandler;
    boolean alreadyLoad;
    String prodName;
    String preOrder;
    int selectedCurrency;
    String prodPrice;
    String prodWeight;
    int selectedWeightUnit;
    String selectedWeightUnitDesc;
    String minimumOrder;
    List<WholeSaleAdapterModel> wholeSaleAdapterModels;
    String newEtalase;
    int returnPolicy_;
    int condition;
    int insurance;
    String description;
    boolean isLoading;
    String productId;
    List<List<SimpleTextModel>> etalaseOwned;
    List<List<SimpleTextModel>> categoryOwned;
    public AddProductType addProductType;
    List<String> textToDisplay;
    List<String> conditions;
    List<String> insurances;
    InstagramMediaModel instagramMediaModel;
    String errorMessageTemp; // use this if want to show at onCreateView
    private long productDb = -1; // for modify product

    /**
     * this flag define after submit the data to the ws.
     * for example, if user tap save and add then it will move to new fresh add product
     * for example, if user tap save then it will move to manage product
     */
    boolean isCreateNewActivity = false;
    public TwitterHandler th;
    private ShareSocmedHandler shareSocmed;

    @OnTextChanged(R2.id.add_product_add_to_new_etalase)
    public void onTextChangedEtalase(CharSequence s, int start, int before,
                                     int count) {
        Pair<Boolean, String> validate = VerificationUtils.validateNewEtalaseName(getActivity(), s.toString());
        if (validate.getModel1()) {
            addProductAddToNewEtalase.setError(null);
            addProductAddToNewEtalaseAlert.setError(null);
        } else {
            addProductAddToNewEtalase.setError(validate.getModel2());
            addProductAddToNewEtalaseAlert.setError(validate.getModel2());
        }
    }

    @Deprecated
    public static Fragment newInstance() {
        return new AddProductFragment();
    }

    /**
     * @param type {@link AddProductType#ADD}, {@link AddProductType#EDIT}
     * @return fragment with certain type
     */
    public static Fragment newInstance(int type) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putInt(ADD_PRODUCT_TYPE, type);
        adf.setArguments(bundle);
        return adf;
    }

    /**
     * @param type {@link AddProductType#ADD}, {@link AddProductType#EDIT}
     * @return fragment with certain type
     */
    public static Fragment newInstance(int type, String imagePathFromImport, int position) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putString(ADD_PRODUCT_IMAGE_PATH, imagePathFromImport);
        bundle.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        adf.setArguments(bundle);
        return adf;
    }

    /**
     * For multiple image
     *
     * @param type
     * @param multiImagePathFromImport
     * @return
     */
    public static Fragment newInstance(int type, String[] multiImagePathFromImport) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putStringArray(ADD_PRODUCT_MULTIPLE_IMAGE_PATH, multiImagePathFromImport);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        adf.setArguments(bundle);
        return adf;
    }

    public static Fragment newInstance(int type, String productId) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putString(PRODUCT_ID, productId);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        adf.setArguments(bundle);
        return adf;
    }

    public static Fragment newInstance(int type, long productDB) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putLong(PRODUCT_DB, productDB);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        adf.setArguments(bundle);
        return adf;
    }

    /**
     * created for socmed photos
     *
     * @param {@link AddProductType#ADD_FROM_SOCIAL_MEDIA}
     * @return fragment with certain type
     */
    @Deprecated
    public static Fragment newInstance(int type, List<String> socMedDatas, int position) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putInt(ADD_PRODUCT_SOC_MED_POSITION, position);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        bundle.putStringArrayList(ADD_PRODUCT_SOC_MED_DATA, new ArrayList<String>(socMedDatas));
        adf.setArguments(bundle);
        return adf;
    }

    public static Fragment newInstance3(int type, final InstagramMediaModel socMedDatas, int position) {
        ArrayList<InstagramMediaModel> instagramMediaModels = new ArrayList<InstagramMediaModel>();
        for (int i = 0; i < position; i++) {
            InstagramMediaModel inst = new InstagramMediaModel();
            instagramMediaModels.add(inst);
        }

        instagramMediaModels.add(socMedDatas);

        return newInstance2(type, instagramMediaModels, position);
    }

    /**
     * created for socmed photos
     *
     * @param {@link AddProductType#ADD_FROM_SOCIAL_MEDIA}
     * @return fragment with certain type
     */
    @Deprecated
    public static Fragment newInstance2(int type, List<InstagramMediaModel> socMedDatas, int position) {
        AddProductFragment adf = new AddProductFragment();
        Bundle bundle = new Bundle();

        // throw exception if you are not registered your type at AddProductType
        boolean isRegistered = false;
        A:
        for (int i = 0; i < AddProductType.values().length; i++) {
            if (type == AddProductType.values()[i].getType()) {
                isRegistered = true;
                break A;
            }
        }
        if (!isRegistered)
            throw new RuntimeException("please update AddProductType to support your type");

        bundle.putInt(ADD_PRODUCT_SOC_MED_POSITION, position);
        bundle.putInt(ADD_PRODUCT_TYPE, type);
        bundle.putParcelable(ADD_PROUCT_SOC_MED_RAW_DATA, Parcels.wrap(new ArrayList<InstagramMediaModel>(socMedDatas)));
        adf.setArguments(bundle);
        return adf;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkNotNull(errorMessageTemp)) {
            Snackbar.make(parentView, errorMessageTemp, Snackbar.LENGTH_LONG).show();
            errorMessageTemp = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBundle(ImageGalleryFragment.SELECTABLE_TAG, multiSelector.saveSelectionStates());
        //[START] currently disable rotateion
        outState.putBoolean(SAVED_ALREADY_LOAD, true);// 1
        outState.putParcelable(SAVED_IMAGE_MODELS, Parcels.wrap(new ArrayList<ImageModel>(photos)));// 2
        outState.putString(SAVED_PRODUCT_NAME, addProductProductName.getText().toString()); // 3
        outState.putParcelable(SAVED_CATEGORIES, Parcels.wrap(new ArrayList<>(categoryModels)));// 4
        outState.putString(SAVED_PREORDER, addProductEdittextPreorder.getText().toString());// 5

        outState.putInt(SAVED_CURRENCY_UNIT, selectedCurrency);// 6.a
        outState.putString(SAVED_CURRENCY_DESC, selectedCurrencyDesc);// 6.b
        outState.putString(SAVED_PRICE, addProductPrice.getText().toString());// 7
        outState.putString(SAVED_WEIGHT, addProductWeight.getText().toString());// 8

        outState.putInt(SAVED_WEIGHT_UNIT, selectedWeightUnit);// 9.a
        outState.putString(SAVED_WEIGHT_DESC, selectedWeightUnitDesc); // 9.b
        outState.putString(SAVED_MIN_ORDER, addProductMinimumOrder.getText().toString());// 10
        ArrayList<WholeSaleAdapterModel> models = new ArrayList<>();
        for(WholesaleModel model : wholesaleLayout.getDatas()){
            models.add(new WholeSaleAdapterModel(model.getQtyOne(), model.getQtyTwo(), model.getQtyPrice()));
        }
        outState.putParcelable(SAVED_WHOLE_SALE, Parcels.wrap(models));// 11
        if (checkNotNull(displayEtalaseModels))
            outState.putParcelable(SAVED_ETALASES, Parcels.wrap(new ArrayList<TextDeleteModel>(displayEtalaseModels)));// 12
        outState.putString(SAVED_NEW_ETALASE, addProductAddToNewEtalase.getText().toString());// 13
        outState.putInt(SAVED_CONDITION, addProductCondition.getSelectedItemPosition());// 15
        outState.putInt(SAVED_INSURANCE, addProductInsurance.getSelectedItemPosition());// 16
        outState.putString(SAVED_DESCRIPTION, addProductDesc.getText().toString());// 17
        outState.putParcelable(SAVED_CONDITIONS, Parcels.wrap(conditions));
        outState.putParcelable(SAVED_INSURANCES, Parcels.wrap(insurances));
        if (checkNotNull(tkpdProgressDialog))
            outState.putBoolean(SAVED_LOADING, tkpdProgressDialog.isProgress());// 18

        if (checkNotNull(textToDisplay)) {
            ArrayList<SimpleTextModel> simpleTextModels = new ArrayList<>();
            for (String text : textToDisplay) {
                simpleTextModels.add(new SimpleTextModel(text));
            }
            outState.putParcelable(SAVED_RETURN_POLICY_LIST, Parcels.wrap(new ArrayList<>(simpleTextModels)));
        }
        if (checkNotNull(etalaseModels)) {
            outState.putParcelable(SAVED_ETALASE_MODELS, Parcels.wrap(new ArrayList<>(etalaseModels)));// 19
        }
        if (checkNotNull(ProductDb))
            outState.putLong(SAVED_PRODUCT_DB_ID, ProductDb.getId());// 20
        outState.putParcelable(SAVED_NEXT_CATEGORY_CHOOSER, Parcels.wrap(categoryAdapter.getParcelFormat()));// 21
        outState.putParcelable(SAVED_NEXT_ETALASE_CHOOSER, Parcels.wrap(etalaseAdapter.getParcelFormat()));// 22
        if (checkNotNull(catalogs))
            outState.putParcelable(SAVED_CATALOGS, Parcels.wrap(catalogs));// 23
        outState.putBoolean(CREATE_NEW_AFTER_FINISH, isCreateNewActivity);// 24
        outState.putInt(PRODUCT_TYPE, addProductType.getType());
        //[END] currently disable rotateion
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProduct = new AddProductPresenterImpl(this);
        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //[START] currently disable rotateion
        if (checkNotNull(savedInstanceState)) {
            alreadyLoad = savedInstanceState.getBoolean(SAVED_ALREADY_LOAD, false);// 1
            photos = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_IMAGE_MODELS));// 2
            prodName = savedInstanceState.getString(SAVED_PRODUCT_NAME, "");// 3
            categoryModels = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_CATEGORIES));// 4
            preOrder = savedInstanceState.getString(SAVED_PREORDER, "");// 5
            selectedCurrency = savedInstanceState.getInt(SAVED_CURRENCY_UNIT, -1);// 6.a
            selectedCurrencyDesc = savedInstanceState.getString(SAVED_CURRENCY_DESC
                    , getResources().getStringArray(R.array.currency_type)[0]); // 6.b
            prodPrice = savedInstanceState.getString(SAVED_PRICE);// 7
            prodWeight = savedInstanceState.getString(SAVED_WEIGHT); // 8
            selectedWeightUnit = savedInstanceState.getInt(SAVED_WEIGHT_UNIT);// 9.a
            selectedWeightUnitDesc = savedInstanceState.getString(SAVED_WEIGHT_DESC
                    , getResources().getStringArray(R.array.weight)[0]); // 9.b
            minimumOrder = savedInstanceState.getString(SAVED_MIN_ORDER);// 10
            wholeSaleAdapterModels = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_WHOLE_SALE));// 11
            displayEtalaseModels = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ETALASES));// 12
            newEtalase = savedInstanceState.getString(SAVED_NEW_ETALASE);// 13
            returnPolicy_ = savedInstanceState.getInt(SAVED_RETURN_POLICY);// 14
            condition = savedInstanceState.getInt(SAVED_CONDITION);// 15
            insurance = savedInstanceState.getInt(SAVED_INSURANCE);// 16
            description = savedInstanceState.getString(SAVED_DESCRIPTION);// 17
            isLoading = savedInstanceState.getBoolean(SAVED_LOADING, false);// 18
            List<SimpleTextModel> simpleTextModels = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_RETURN_POLICY_LIST));// 19
            textToDisplay = new ArrayList<>();
            if (checkNotNull(simpleTextModels))// 20
            {
                for (SimpleTextModel simpleTextModel : simpleTextModels) {
                    textToDisplay.add(simpleTextModel.getText());
                }
            }

            conditions = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_CONDITIONS));// 21
            insurances = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_INSURANCES));// 22
            etalaseModels = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ETALASE_MODELS));// 23
            long dbId = savedInstanceState.getLong(SAVED_PRODUCT_DB_ID, DEFAULT_INVALID_VALUE);// 24
            if (dbId != DEFAULT_INVALID_VALUE) {
                ProductDb = DbManagerImpl.getInstance().getProductDb(dbId);
            }
            etalaseOwned = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_NEXT_ETALASE_CHOOSER));
            categoryOwned = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_NEXT_CATEGORY_CHOOSER));
            catalogs = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_CATALOGS));
            isCreateNewActivity = savedInstanceState.getBoolean(CREATE_NEW_AFTER_FINISH, false);
        }
        //[END] currently disable rotateion
        th = new TwitterHandler(getActivity());
    }

    /**
     * hide keyboard if not edittext
     *
     * @param view
     */
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    FragmentActivity activity = AddProductFragment.this.getActivity();
                    if (keyboardHandler != null && activity != null) {
                        keyboardHandler.hideSoftKeyboard(activity);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    private void clearFocusable() {
        addProductAddToNewEtalase.clearFocus();
        addProductMinimumOrder.clearFocus();
        addProductProductName.clearFocus();
        addProductPrice.clearFocus();
        addProductWeight.clearFocus();
        addProductDesc.clearFocus();
        addProductEdittextPreorder.clearFocus();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        keyboardHandler = new KeyboardHandler();
        addProduct.subscribe();
        Bundle bundle = getArguments();
        int type = -1;
        if (checkNotNull(bundle) && (type = bundle.getInt(ADD_PRODUCT_TYPE, -1)) != -1) {
            if (type == AddProductType.ADD.getType()) {
                addProductType = AddProductType.ADD;
            } else if (type == AddProductType.ADD_FROM_SOCIAL_MEDIA.getType()) {
                addProductType = AddProductType.ADD_FROM_SOCIAL_MEDIA;
            } else if (type == AddProductType.EDIT.getType()) {
                addProductType = AddProductType.EDIT;
            } else if (type == AddProductType.COPY.getType()) {
                addProductType = AddProductType.COPY;
            } else if (type == AddProductType.ADD_FROM_GALLERY.getType()) {
                addProductType = AddProductType.ADD_FROM_GALLERY;
            } else if (type == AddProductType.ADD_MULTIPLE_FROM_GALERY.getType()) {
                addProductType = AddProductType.ADD_MULTIPLE_FROM_GALERY;
            } else if (type == AddProductType.MODIFY.getType()) {
                addProductType = AddProductType.MODIFY;
            }
        } else {
            throw new RuntimeException("please register your type at AddProductType");
        }

        changeTitle(addProductType.getType());

        parentView = inflater.inflate(R.layout.fragment_add_product, container, false);

        unbinder = ButterKnife.bind(this, parentView);

        if (addProductType != null) {
            if (type == AddProductType.ADD.getType()) {
                addProductShareContainer = new AddProductShare(addProductShare.inflate());
                addProductShareContainer.setDelegateOnClick(this);
                addProductSubmitContainer = new AddProductSubmit(addProductSubmit.inflate());
                addProductSubmitContainer.setDelegateOnClick(this);
            } else if (type == AddProductType.ADD_FROM_SOCIAL_MEDIA.getType()) {
                addProductSocMedSubmitContainer
                        = new AddProductSocMedSubmit(addProductSocMedSubmit.inflate());
                addProductSocMedSubmitContainer.setDelegateOnClick(this);

                if (!alreadyLoad) {
                    List<InstagramMediaModel> temp2 = Parcels.unwrap(
                            bundle.getParcelable(ADD_PROUCT_SOC_MED_RAW_DATA));
                    int position = bundle.getInt(ADD_PRODUCT_SOC_MED_POSITION, 0);
                    photos = new ArrayList<>();
                    instagramMediaModel = temp2.get(position);
                    downloadImage(temp2.get(position).standardResolution);
                    photos.add(new ImageModel(temp2.get(position).standardResolution));

                    //[OLD CODE]
//                    List<String> temp = bundle.getStringArrayList(ADD_PRODUCT_SOC_MED_DATA);
//                    int position = bundle.getInt(ADD_PRODUCT_SOC_MED_POSITION, 0);
//                    photos = new ArrayList<>();
//                    downloadImage(temp.get(position));
//                    photos.add(new ImageModel(temp.get(position)));
                    //[OLD CODE]
                }
                // get the position for dismiss add product
                positionAtSocMed = bundle.getInt(ADD_PRODUCT_SOC_MED_POSITION, 0);
            } else if (type == AddProductType.EDIT.getType()) {
                if (!alreadyLoad) {
                    productId = bundle.getString(PRODUCT_ID, "XXX");
                }

                addProductSocMedSubmitContainer
                        = new AddProductSocMedSubmit(addProductSocMedSubmit.inflate());
                addProductSocMedSubmitContainer.setDelegateOnClick(this);

                addProductProductName.setEnabled(false);

                // show dialog for edit product.
                tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS, getResources().getString(R.string.edit_product));
                tkpdProgressDialog.showDialog();
            } else if (type == AddProductType.COPY.getType()) {
                if (!alreadyLoad) {
                    productId = bundle.getString(PRODUCT_ID, "XXX");
                }

                addProductShareContainer = new AddProductShare(addProductShare.inflate());
                addProductShareContainer.setDelegateOnClick(this);
                addProductSubmitContainer = new AddProductSubmit(addProductSubmit.inflate());
                addProductSubmitContainer.setDelegateOnClick(this);

                // show dialog for copy product.
                tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS, getResources().getString(R.string.edit_product));
                tkpdProgressDialog.showDialog();
            } else if (type == AddProductType.ADD_FROM_GALLERY.getType()) {
                addProductShareContainer = new AddProductShare(addProductShare.inflate());
                addProductShareContainer.setDelegateOnClick(this);
                addProductSubmitContainer = new AddProductSubmit(addProductSubmit.inflate());
                addProductSubmitContainer.setDelegateOnClick(this);

                if (!alreadyLoad) {
                    String imagePathFromImport = bundle.getString(ADD_PRODUCT_IMAGE_PATH);
                    int position = bundle.getInt(ADD_PRODUCT_IMAGE_LOCATION);
                    Log.d(TAG, messageTAG + " imagePath [" + imagePathFromImport + "] position [" + position + "]");
                    photos = new ArrayList<>();
                    File imagePath = new File(imagePathFromImport);
                    if (checkFileSize(imagePath)) {
                        try {
                            ImageGalleryImpl.Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(getActivity(), imagePathFromImport);
                            if (imagePathFromImport != null && checkImageResolution.getModel1()) {
                                photos.add(getImageModelPrimary(imagePathFromImport, imagePath));
                            } else {
                                errorMessageTemp = checkImageResolution.getModel2();
                                Log.e(ImageGalleryView.TAG, messageTAG + checkImageResolution.getModel2());
                            }
                        } catch (MetadataUtil.UnSupportedimageFormatException e) {
                            e.printStackTrace();
                            errorMessageTemp = e.getMessage();
                            Log.e(ImageGalleryView.TAG, messageTAG + e.getMessage());
                        }
                    } else {
                        CommonUtils.UniversalToast(getActivity(), "Gambar Terlalu Besar");
                    }
                }
            } else if (type == AddProductType.ADD_MULTIPLE_FROM_GALERY.getType()) {
                addProductShareContainer = new AddProductShare(addProductShare.inflate());
                addProductShareContainer.setDelegateOnClick(this);
                addProductSubmitContainer = new AddProductSubmit(addProductSubmit.inflate());
                addProductSubmitContainer.setDelegateOnClick(this);

                if (!alreadyLoad) {
                    String[] multiImagePathFromImport = bundle.getStringArray(ADD_PRODUCT_MULTIPLE_IMAGE_PATH);
                    Log.d(TAG, messageTAG + " imagePath [" + multiImagePathFromImport + "]");
                    photos = new ArrayList<>();
                    boolean primaryCreated = false;
                    for (String imagePathFromImport :
                            multiImagePathFromImport) {
                        if (imagePathFromImport != null) {
                            File imagePath = new File(imagePathFromImport);
                            if (checkFileSize(imagePath)) {
                                try {
                                    ImageGalleryImpl.Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(getActivity(), imagePathFromImport);
                                    if (checkImageResolution.getModel1()) {
                                        if (!primaryCreated) {
                                            photos.add(getImageModelPrimary(imagePathFromImport, imagePath));
                                            primaryCreated = true;
                                        } else {
                                            photos.add(getImageModel(imagePathFromImport, imagePath));
                                        }
                                    } else {
                                        errorMessageTemp = checkImageResolution.getModel2();
                                        Log.e(ImageGalleryView.TAG, messageTAG + checkImageResolution.getModel2());
                                    }
                                } catch (MetadataUtil.UnSupportedimageFormatException e) {
                                    e.printStackTrace();
                                    errorMessageTemp = e.getMessage();
                                    Log.e(ImageGalleryView.TAG, messageTAG + e.getMessage());
                                }
                            } else {
                                CommonUtils.UniversalToast(getActivity(), "Gambar Terlalu Besar");
                            }
                        }
                    }
                }
            } else if (type == AddProductType.MODIFY.getType()) {
                addProductShareContainer = new AddProductShare(addProductShare.inflate());
                addProductShareContainer.setDelegateOnClick(this);
                addProductSubmitContainer = new AddProductSubmit(addProductSubmit.inflate());
                addProductSubmitContainer.setDelegateOnClick(this);


                if (!alreadyLoad) {
                    productDb = bundle.getLong(PRODUCT_DB, -1);
                }

            }
        }

        addProductDesc.setMaxLines(2000);
        addProductDesc.setMaxCharacters(2000);

        initVar();
        setupUI(addProductParent);
        return parentView;
    }

    private static boolean checkFileSize(File imagePath) {
        int fileSize = Integer.parseInt(String.valueOf(imagePath.length() / 1024));
        Log.d(TAG, "File size" + fileSize);
        if (fileSize < 10000) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void dismissDialog() {
        if (checkNotNull(tkpdProgressDialog) && tkpdProgressDialog.isProgress()) {
            tkpdProgressDialog.dismiss();
        }
    }

    public void dismissReturnableDialog() {
        ReturnPolicyDialog returnPolicyDialog = ((ReturnPolicyDialog) getActivity().getSupportFragmentManager().findFragmentByTag(ReturnPolicyDialog.FRAGMENT_TAG));
        if (returnPolicyDialog != null) {
            returnPolicyDialog.dismiss();
        }

    }

    private void changeTitle(int type) {
        if (type == AddProductType.EDIT.getType()) {
            getActivity().setTitle(R.string.title_activity_edit_product);
        } else {
            getActivity().setTitle(R.string.title_activity_add_product);
        }
    }

    /**
     * This method specific for socmed only
     *
     * @param url
     */
    private void downloadImage(final String url) {
        // if not edit then do nothing
        if (!addProductType.equals(AddProductType.ADD_FROM_SOCIAL_MEDIA))
            return;

        Observable.just(true)
                .map(new Func1<Boolean, File>() {
                    @Override
                    public File call(Boolean aBoolean) {
                        FutureTarget<File> future = Glide.with(getActivity())
                                .load(url)
                                .downloadOnly(4096, 2160);
                        File photo = null;
                        try {
                            File cacheFile = future.get();
                            photo = UploadPhotoTask.writeImageToTkpdPath(cacheFile);
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
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<File>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, messageTAG + e.getMessage());
                            }

                            @Override
                            public void onNext(File file) {
                                photos = new ArrayList<ImageModel>() {
                                    {
                                        for (int i = 0; i < 5; i++)
                                            add(new ImageModel(R.drawable.addproduct));
                                    }
                                };
                                try {
                                    ImageGalleryImpl.Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(getActivity(), file.getAbsolutePath());
                                    if (file.getAbsolutePath() != null && checkImageResolution.getModel1()) {
                                        photos.set(0, getImageModel(file.getAbsolutePath(), file));
                                    } else {
                                        Snackbar.make(parentView, checkImageResolution.getModel2(), Snackbar.LENGTH_LONG).show();
                                        Log.e(ImageGalleryView.TAG, messageTAG + checkImageResolution.getModel2());
                                    }
                                } catch (MetadataUtil.UnSupportedimageFormatException e) {
                                    e.printStackTrace();
                                    Snackbar.make(parentView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    Log.e(ImageGalleryView.TAG, messageTAG + e.getMessage());
                                }
                                initPhotoAdapter(photos);
                            }
                        }
                );
    }

    @Override
    public void addEtalaseAfterSelect(SimpleTextModel model) {
        addProductAddTo.requestFocus();
        switch (model.getLevel()) {
            case LEVEL_ONE:
                TextDeleteModel deleteModel = new TextDeleteModel(model.getText());
                deleteModel.setDataPosition(model.getPosition());
                displayEtalaseModels.clear();
                TextDeleteModel textDeleteModel = saveEtalase(model);
                textDeleteModel.setCustomText(getActivity().getResources().getString(R.string.title_action_add_status));
                displayEtalaseModels.add(textDeleteModel);
                List<SimpleTextModel> lvl2 = toSimpleTextEtalase(etalaseModels, LEVEL_TWO, model.getPosition());
                // if next step is avaiable
                if (lvl2 != null && lvl2.size() > 0) {// show another spinner
                    displayEtalaseModels.add(new TextDeleteModel(true));
                    List<SimpleTextModel> lvl1 = etalaseAdapter.getSimpleTextModels(0);
                    etalaseAdapter.clearSimpleTextModels();//
                    etalaseAdapter.setSimpleTextModels(lvl1);
                    etalaseAdapter.setSimpleTextModels(lvl2);
                }
                etalaseAdapter.notifyDataSetChanged();
                break;
            case LEVEL_TWO:
                deleteModel = new TextDeleteModel(model.getText());
                deleteModel.setDataPosition(model.getPosition());
                TextDeleteModel deleteModelLvl1 = displayEtalaseModels.get(0);
                displayEtalaseModels.clear();
                displayEtalaseModels.add(deleteModelLvl1);// level 1
                displayEtalaseModels.add(saveEtalase(model));// level 3
                etalaseAdapter.notifyDataSetChanged();

                if (model.getText().contains(TAMBAH_ETALASE_BARU)) {
                    addProductAddToNewEtalase.setEnabled(true);
                    addProductAddToNewEtalaseLayout.expand();
                } else {
                    addProductAddToNewEtalase.setEnabled(false);
                    addProductAddToNewEtalaseLayout.collapse();
                }
                break;
        }
    }

    private TextDeleteModel saveEtalase(SimpleTextModel model) {
        EtalaseDB etalaseDB = new Select().from(EtalaseDB.class).where(
                EtalaseDB_Table.etalse_name.eq(model.getText())
        ).querySingle();
        TextDeleteModel textDeleteModel = new TextDeleteModel(model.getText());
        textDeleteModel.setDataPosition(model.getPosition());
        if (etalaseDB != null)
            textDeleteModel.setEtalaseId(etalaseDB.getId());
        return textDeleteModel;
    }

    public void addCategoryAfterSelectV2(SimpleTextModel model) {
        addProductCategorySpinner.requestFocus();
        int level = model.getLevel();
        fetchDepartmentChild(model.getPosition(), ++level);
    }

    private TextDeleteModel saveCategory(SimpleTextModel model) {
        //[START] query based on name
        CategoryDB categoryDB = new Select().from(CategoryDB.class).where(
                CategoryDB_Table.nameCategory.eq(model.getText())
        ).querySingle();
        String d_id = categoryDB.getDepartmentId() + "";
        String parent = categoryDB.getParentId() + "";
        Log.d(TAG, messageTAG + "kategori : " + model.getText() + " d_id : " + d_id + " parent " + parent);
        //[END] query based on name

        TextDeleteModel textDeleteModel = new TextDeleteModel(model.getText());
        textDeleteModel.setDataPosition(model.getPosition());
        CategoryDB existingCategoryDB = new Select().from(CategoryDB.class).where(
                CategoryDB_Table.nameCategory.eq(model.getText())
        ).querySingle();
        if (existingCategoryDB == null) {
            CategoryDB categoryDB1 = new CategoryDB(model.getText(), model.getLevel(), -1, Integer.parseInt(parent), Integer.parseInt(d_id), "");
            categoryDB1.save();
            long id = categoryDB1.Id;
            existingCategoryDB = new Select().from(CategoryDB.class).where(
                    CategoryDB_Table.nameCategory.eq(model.getText())
            ).querySingle();
        }
        textDeleteModel.setDepartmentId(existingCategoryDB.departmentId);
        return textDeleteModel;
    }

    @Override
    public void setProductPreOrder(String preOrderDay) {
        addProductEdittextPreorder.setText(preOrderDay);
    }


    public void addImageAfterSelect(ArrayList<String> path) {
        List<Pair<Integer, String>> result = new ArrayList<>();
        List<Integer> defaultPositions = ImageModel.determineDefault(photos);
        int count = 0;
        int sizeToIterate = -1;
        if (path.size() <= defaultPositions.size()) {
            sizeToIterate = path.size();
        } else {
            sizeToIterate = defaultPositions.size();
        }
        for (int i = 0; i < sizeToIterate; i++) {
            result.add(new Pair<Integer, String>(defaultPositions.get(i), path.get(i)));
            count++;
        }

        Observable.just(result)
                .take(5)
                .map(new Func1<List<Pair<Integer, String>>, Object>() {
                    @Override
                    public Object call(List<Pair<Integer, String>> pairList) {
                        for (Pair<Integer, String> temp :
                                pairList) {
                            addImageAfterSelect(temp.getModel2(), temp.getModel1());
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe();
    }

    @Override
    public void addImageAfterSelect(String path, int position) {
        try {
            Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(getActivity(), path);

            if (path != null && checkImageResolution.getModel1()) {
                moveToTkpdPath(path, position);
                if (position == 0) {
                    setSelectedImageAsPrimary(0);
                }
            } else {
                Snackbar.make(parentView, checkImageResolution.getModel2(), Snackbar.LENGTH_LONG).show();
                Log.e(TAG, messageTAG + checkImageResolution.getModel2());
            }
        } catch (MetadataUtil.UnSupportedimageFormatException usie) {
            Snackbar.make(parentView, usie.getMessage(), Snackbar.LENGTH_LONG).show();
            Log.e(TAG, messageTAG + usie.getMessage());
        }
    }

    private void moveToTkpdPath(String path, final int position) {
        File photo = UploadPhotoTask.writeImageToTkpdPath(compressImage(path));
        if (photo != null) {
            try {
                ImageGalleryImpl.Pair<Boolean, String> checkImageResolution = VerificationUtils.checkImageResolution(getActivity(), path);
                if (path != null && checkImageResolution.getModel1()) {
                    //[START] save to db for images
                    ImageModel newPhoto = getImageModel(path, photo);
                    ImageModel oldPhoto = photos.get(position);

                    int positionToFillImage = position;
                    for(int i=photos.size()-1; i>=0; i--){
                        ImageModel imageModel = photos.get(i);
                        if(imageModel.getPath() == null){
                            positionToFillImage = i;
                        }
                    }
                    photos.set(positionToFillImage, newPhoto);

                    //[START] recreate the adapter
                    photoAdapter = new PhotoAdapter(photos);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), com.tkpd.library.ui.view.LinearLayoutManager.HORIZONTAL, false);
                    addProductImages.setLayoutManager(layoutManager);
                    addProductImages.setAdapter(photoAdapter);
                    //[START] recreate the adapter

                    if (addProductType.equals(AddProductType.EDIT)) {
                        producteditHelper.deleteImage(oldPhoto, position);
                        producteditHelper.addImage(newPhoto, positionToFillImage);
                    }
                } else {
                    Snackbar.make(parentView, checkImageResolution.getModel2(), Snackbar.LENGTH_LONG).show();
                    Log.e(ImageGalleryView.TAG, messageTAG + checkImageResolution.getModel2());
                }
            } catch (MetadataUtil.UnSupportedimageFormatException e) {
                e.printStackTrace();
                Snackbar.make(parentView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.e(ImageGalleryView.TAG, messageTAG + e.getMessage());
            }
        } else {
            Log.e(TAG, messageTAG + "unable to save to tkpd path !!!");
        }
    }

    @NonNull
    public static ImageModel getImageModel(String path, File photo) throws MetadataUtil.UnSupportedimageFormatException {
        Pair<Integer, Integer> resolution = null;
        resolution = MetadataUtil.getWidthFromImage(photo.getAbsolutePath());
        int width = resolution.getModel1();
        int height = resolution.getModel2();
        PictureDB pictureDB = new PictureDB(photo.getAbsolutePath(), width, height);
        pictureDB.save();
        Long saveGambar = pictureDB.getId();
        Log.d(TAG, messageTAG + " hasil save ke db : " + saveGambar);
        //[END] save to db for images
        ImageModel imageModel = new ImageModel();
        imageModel.setPath(path);
        imageModel.setDbId(saveGambar);
        return imageModel;
    }

    @NonNull
    public static ImageModel getImageModelPrimary(String path, File photo) throws MetadataUtil.UnSupportedimageFormatException {
        Pair<Integer, Integer> resolution = null;
        resolution = MetadataUtil.getWidthFromImage(photo.getAbsolutePath());
        int width = resolution.getModel1();
        int height = resolution.getModel2();
        PictureDB pictureDB = new PictureDB(photo.getAbsolutePath(), width, height);
        pictureDB.setPicturePrimary(PictureDB.PRIMARY_IMAGE);
        pictureDB.save();
        Long saveGambar = pictureDB.getId();
        Log.d(TAG, messageTAG + " hasil save ke db : " + saveGambar);
        //[END] save to db for images
        ImageModel imageModel = new ImageModel();
        imageModel.setPath(path);
        imageModel.setDbId(saveGambar);
        return imageModel;
    }

    @NonNull
    public static ImageModel getImageModel(String path, File photo, EditProductForm.ProductImage productImage) {
        try {
            Pair<Integer, Integer> resolution = MetadataUtil.getWidthFromImage(photo.getAbsolutePath());
            int width = resolution.getModel1();
            int height = resolution.getModel2();
            PictureDB savePictureDB = new PictureDB(photo.getAbsolutePath(), width, height,
                    productImage.getImageId(), productImage.getImagePrimary(),
                    productImage.getImageSrc(), productImage.getImageSrc300(),
                    productImage.getImageDescription());
            savePictureDB.save();
            Long saveGambarLong = savePictureDB.getId();
            Log.d(TAG, messageTAG + " hasil save ke db : " + saveGambarLong);
            //[END] save to db for images
            ImageModel imageModel = new ImageModel();
            imageModel.setPath(path);
            imageModel.setDbId(saveGambarLong);
            return imageModel;
        } catch (MetadataUtil.UnSupportedimageFormatException e) {
            return null;
        }
    }

    public static byte[] compressImage(String path) {
        Log.d(TAG, "lokasi yang mau diupload " + path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;
        if (tempPic != null) {
            try {
                tempPic = new ImageHandler().RotatedBitmap(tempPic, path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
                tempPicToUpload = new ImageHandler().ResizeBitmap(tempPic, 2048);
            }
            else {
                tempPicToUpload = tempPic;
            }
            tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
            return bao.toByteArray();
        }
        return null;
    }

    @Override
    public void initVar() {
        RecyclerView.LayoutManager layoutManager = null;
        if (!alreadyLoad) {

            // set minimum order to 1
            addProductMinimumOrder.setText("1");

            //[START] create empty category model
            categoryModels = new ArrayList<>();
            initCategoryAdapter(categoryModels);
            //[END] create empty category model


            insurances = new ArrayList<>();
            for (String temp : getResources().getStringArray(R.array.insurance)) {
                insurances.add(temp);
            }
            addProductInsurance.setAdapter(SimpleSpinnerAdapter.createAdapterAddProduct(getActivity(), insurances));


            conditions = new ArrayList<>();
            for (String temp : getResources().getStringArray(R.array.condition)) {
                conditions.add(temp);
            }
            addProductCondition.setAdapter(SimpleSpinnerAdapter.createAdapterAddProduct(getActivity(), conditions));


            switch (addProductType) {
                case EDIT:
                case ADD:
                case COPY:
                case MODIFY:
                    photos = new ArrayList<ImageModel>() {
                        {
                            for (int i = 0; i < 5; i++)
                                add(new ImageModel(R.drawable.addproduct));
                        }
                    };
                    break;
                case ADD_FROM_SOCIAL_MEDIA:
                case ADD_FROM_GALLERY:
                    Log.d(TAG, messageTAG + " already init from arguments : " + photos);
                    ImageModel firstImage = null;
                    if (photos.size() > 0) {
                        firstImage = photos.get(0);
                    }
                    photos = new ArrayList<ImageModel>() {
                        {
                            for (int i = 0; i < 5; i++)
                                add(new ImageModel(R.drawable.addproduct));
                        }
                    };
                    if (checkNotNull(firstImage))
                        photos.set(0, firstImage);

                    if (instagramMediaModel != null) {
                        addProductDesc.setText(instagramMediaModel.captionText);
                    }
                    break;
                case ADD_MULTIPLE_FROM_GALERY:
                    Log.d(TAG, messageTAG + " already init from arguments : " + photos);

                    photos = new ArrayList<ImageModel>() {
                        {
                            for (int i = 0; i < photos.size(); i++) {
                                ImageModel image = photos.get(i);
                                add(image);
                            }
                        }
                    };
                    int remainSize = 5 - photos.size();
                    for (int i = 0; i < remainSize; i++) {
                        photos.add(new ImageModel(R.drawable.addproduct));
                    }
                    break;
                default:
                    break;
            }

            initPhotoAdapter(photos);


            displayEtalaseModels = new ArrayList<>();
//            retrieveDepartmentIdWithName();

            //[START] this is for demo only
            initWholeSaleAdapter();
            //[END] this is for demo only

//            addEtalaseChooseText();
            initEtalaseAdapter(new ArrayList<EtalaseModel>());
//            fetchEtalase();

//            checkAvailibilityOfShopNote();

            initWeightUnit();
            initCurrencyUnit();

            fetchDepartmentParent();
        } else {
            photoAdapter = new PhotoAdapter(photos);
            layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
            addProductImages.setLayoutManager(layoutManager);
            addProductImages.setAdapter(photoAdapter);

            addProductProductName.setText(prodName);

            initCategoryAdapter(categoryModels);

            for (List<SimpleTextModel> a : categoryOwned) {
                categoryAdapter.setSimpleTextModels(a);
            }

            addProductEdittextPreorder.setText(preOrder);

            initCurrencyUnit(getResources().getStringArray(R.array.currency_type));
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addProductPrice.setText(prodPrice);
                }
            }, 50);
            dismissPriceError();

            initWeightUnit(getResources().getStringArray(R.array.weight));
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addProductWeight.setText(prodWeight);
                }
            }, 50);
            dismissWeightError();

            addProductMinimumOrderAlert.setError(null);
            addProductMinimumOrderAlert.setErrorEnabled(false);
            addProductMinimumOrder.setText(minimumOrder);

            initWholeSaleAdapter();

            etalaseAdapter = new TextDeleteAdapter(displayEtalaseModels, ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE);
//            etalaseAdapter.setSimpleTextModels(toSimpleTextEtalase(etalaseModels, 0));
            for (List<SimpleTextModel> a : etalaseOwned) {
                etalaseAdapter.setSimpleTextModels(a);
            }
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            addProductAddTo.setLayoutManager(layoutManager);
            addProductAddTo.setAdapter(etalaseAdapter);

            addProductAddToNewEtalase.setText(newEtalase);

            initReturnableSpinner(textToDisplay);

            addProductCondition.setAdapter(SimpleSpinnerAdapter.createAdapterAddProduct(getActivity(), conditions));
            addProductCondition.setSelection(condition);

            addProductInsurance.setAdapter(SimpleSpinnerAdapter.createAdapterAddProduct(getActivity(), insurances));
            addProductInsurance.setSelection(insurance);

            addProductDesc.setText(description);
            addProductProductDescLayout.setError(null);
            addProductProductDescLayout.setErrorEnabled(false);

            if (ProductDb != null && ProductDb.getProductId() != 0 && isLoading) {
                showProgress(true);
            } else {
                showProgress(false);

            }

            setToCatalogView(catalogs);
        }

    }

    @Override
    public void initWholeSaleAdapter(ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels) {
        List<WholesaleModel> models = new ArrayList<>();
        for (WholeSaleAdapterModel model : wholeSaleAdapterModels){
            models.add(new WholesaleModel(
                    Double.valueOf(model.getQuantityOne()).intValue(),
                    Double.valueOf(model.getQuantityTwo()).intValue(),
                    model.getWholeSalePrice()));
        }
        Double price = addProductPrice.getText().toString().isEmpty()? 0 : Double.parseDouble(CurrencyFormatter.getRawString(addProductPrice.getText().toString()));
        int currency = (selectedCurrencyDesc == null || selectedCurrencyDesc.equals("Rp") ? PriceUtils.CURRENCY_RUPIAH : PriceUtils.CURRENCY_DOLLAR);
        wholesaleLayout.setupParams(
                price,
                currency,
                models);
    }

    public void initWholeSaleAdapter() {
        Double price = addProductPrice.getText().toString().isEmpty()? 0 : Double.parseDouble(CurrencyFormatter.getRawString(addProductPrice.getText().toString()));
        int currency = (selectedCurrencyDesc == null || selectedCurrencyDesc.equals("Rp") ? PriceUtils.CURRENCY_RUPIAH : PriceUtils.CURRENCY_DOLLAR);
        wholesaleLayout.setupParams(
                price,
                currency);
    }

    @Override
    public void initCategoryAdapter(@NonNull ArrayList<TextDeleteModel> categoryModels) {
        if (!checkNotNull(categoryModels))
            return;

        categoryAdapter = new TextDeleteAdapter(categoryModels);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addProductCategorySpinner.setLayoutManager(layoutManager);
        addProductCategorySpinner.setAdapter(categoryAdapter);
    }

    @Override
    public void initCategoryAdapter(List<List<SimpleTextModel>> textDeleteModels, @NonNull ArrayList<TextDeleteModel> categoryModels) {
        if (!checkNotNull(categoryModels))
            return;

        if (!checkNotNull(textDeleteModels))
            return;

        this.categoryModels = categoryModels;
        categoryAdapter = new TextDeleteAdapter(categoryModels);
        for (List<SimpleTextModel> temp : textDeleteModels) {
            categoryAdapter.setSimpleTextModels(temp);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addProductCategorySpinner.setLayoutManager(layoutManager);
        addProductCategorySpinner.setAdapter(categoryAdapter);
    }

    /**
     * ini photo adapter
     */
    public void initPhotoAdapter(List<ImageModel> datas) {
        // save to the datas
        this.photos = new ArrayList<>(datas);

        RecyclerView.LayoutManager layoutManager;
        photoAdapter = new PhotoAdapter(datas);
        layoutManager = new LinearLayoutManager(getActivity(), com.tkpd.library.ui.view.LinearLayoutManager.HORIZONTAL, false);
        addProductImages.setLayoutManager(layoutManager);
        addProductImages.setAdapter(photoAdapter);
    }

    @Override
    public void disableProductNameEdit() {
        if (addProductType != AddProductType.COPY) {
            addProductProductName.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void enableProductNameEdit() {
        addProductProductName.setEnabled(true);
        addProductProductName.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void setProductName(String productName) {
        if (addProductType != AddProductType.COPY) {
            addProductProductName.setText(productName);
        }
    }

    @Override
    public void setWeightUnit(String weightUnit) {
        List<SimpleTextModel> weightUnits = addProductWeightUnit.getmItems();
        int count = 0;
        for (SimpleTextModel stm : weightUnits) {
            if (stm.getLabel().toLowerCase().contains("(" + weightUnit + ")")) {
                selectedWeightUnit = count;
                selectedWeightUnitDesc = stm.getLabel();
            }
            count++;
        }

        addProductWeightUnit.setText(selectedWeightUnitDesc);
    }

    @Override
    public void setProductReturnable(boolean returnable) {
    }

    @Override
    public void setProductInsurance(boolean isInsurance) {
        if (isInsurance) {
            addProductInsurance.setSelection(1);
        } else {
            addProductInsurance.setSelection(0);
        }
    }

    @Override
    public void setProductCondition(boolean isNew) {
        if (isNew) {
            addProductCondition.setSelection(0);// baru
        } else {
            addProductCondition.setSelection(1);// bekas
        }
    }

    @Override
    public void setProductEtalase(boolean isGudang, String productEtalaseId) {
        EtalaseDB etalaseDB = DbManagerImpl.getInstance().getEtalase(productEtalaseId);
        if (etalaseDB == null)
            return;

        displayEtalaseModels.clear();
        TextDeleteModel addLevel1 = null;
        TextDeleteModel addLevel2 = null;

        int modelPosition = -1;
        EtalaseDB etalaseDBId = DbManagerImpl.getInstance().getEtalase(productEtalaseId);
        if (isGudang) {
            addLevel1 = new TextDeleteModel(ETALASE_GUDANG);
            addLevel2 = new TextDeleteModel(etalaseDB.getEtalaseName());
            addLevel2.setEtalaseId(etalaseDBId.getId());
            modelPosition = 0;
        } else {
            addLevel1 = new TextDeleteModel(ETALASE_ETALASE);
            addLevel2 = new TextDeleteModel(etalaseDB.getEtalaseName());
            addLevel2.setEtalaseId(etalaseDBId.getId());
            modelPosition = 1;
        }
        addLevel1.setCustomText(getString(R.string.title_action_add_status));
        displayEtalaseModels.add(addLevel1);
        displayEtalaseModels.add(addLevel2);

        List<SimpleTextModel> lvl2 = toSimpleTextEtalase(etalaseModels, LEVEL_TWO, modelPosition);
        // if next step is avaiable
        if (lvl2 != null && lvl2.size() > 0) {// show another spinner
            List<SimpleTextModel> lvl1 = etalaseAdapter.getSimpleTextModels(0);
            etalaseAdapter.clearSimpleTextModels();//
            etalaseAdapter.setSimpleTextModels(lvl1);
            etalaseAdapter.setSimpleTextModels(lvl2);
        }
        etalaseAdapter.notifyDataSetChanged();


    }

    @Override
    public void setProductWeight(String weight) {
        addProductWeight.setText(weight);
    }

    @Override
    public void setProductMinOrder(String minOrder) {
        addProductMinimumOrder.setText(minOrder);
    }

    @Override
    public void setProductDesc(String productDescription) {
        addProductDesc.setText(productDescription);
        addProductDesc.requestFocus();
    }

    @Override
    public void setProductCurrencyUnit(String productCurrencyUnit) {
        if (productCurrencyUnit.equals("Rp")) {
            selectedCurrency = 0;
            selectedCurrencyDesc = "Rp";
        } else {
            selectedCurrency = 1;
            selectedCurrencyDesc = "US$";
        }
        addProductCurrency.setText(selectedCurrencyDesc);
    }

    @Override
    public String getProductCurrencyUnit() {
        return selectedCurrencyDesc;
    }

    @Override
    public void setProductPrice(String productPrice) {

        addProductPrice.setText(productPrice);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (alreadyLoad) {
            List<WholesaleModel> models = new ArrayList<>();
            for (WholeSaleAdapterModel model : wholeSaleAdapterModels) {
                models.add(new WholesaleModel(
                        Double.valueOf(model.getQuantityOne()).intValue(),
                        Double.valueOf(model.getQuantityTwo()).intValue(),
                        model.getWholeSalePrice()
                ));
            }
            wholesaleLayout.setupParams(models);
        }
        dismissErrorProductName();
        dismissPriceError();
        dismissWeightError();
        ScreenTracking.screen(getScreenName());
        addProduct.setupNameDebounceListener(getActivity());
    }

    public void initWeightUnit() {
        String[] weightUnit = getResources().getStringArray(R.array.weight);
        int count = 1;
        for (String weight : weightUnit) {
            WeightUnitDB weightSel = new Select().from(WeightUnitDB.class).where(
                    WeightUnitDB_Table.abrvWeight.eq(weight)
            )
                    .querySingle();
            if (weightSel == null)
                new WeightUnitDB(weight, weight, count++).save();
        }
        initWeightUnit(weightUnit);
    }

    private void initWeightUnit(String[] weightUnit) {

        List<SimpleTextModel> textModels = new ArrayList<>();
        for (String weight : weightUnit) {
            textModels.add(new SimpleTextModel(weight));
        }
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_drop_down_resize);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.5),
                (int) (drawable.getIntrinsicHeight() * 0.5));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 0.1f, 0.1f);
        addProductWeightUnit.setCompoundDrawablesWithIntrinsicBounds(null, null, sd.getDrawable(), null);
        addProductWeightUnit.setItems(textModels);
        addProductWeightUnit.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<SimpleTextModel>() {
            @Override
            public void onItemSelectedListener(SimpleTextModel item, int selectedIndex) {
                selectedWeightUnitDesc = item.getLabel();
                AddProductFragment.this.selectedWeightUnit = selectedIndex;
            }
        });
        addProductWeightUnit.setLongClickable(false);

        selectedWeightUnit = 0;
        selectedWeightUnitDesc = weightUnit[selectedWeightUnit];

        addProductWeightUnit.setText(selectedWeightUnitDesc);
    }

    public void initCurrencyUnit() {
        String[] currencyUnit = getResources().getStringArray(R.array.currency_type);
        int count = 1;
        for (String currency : currencyUnit) {
            CurrencyDB mataUang = new Select().from(CurrencyDB.class).where(
                    CurrencyDB_Table.abrvCurr.eq(currency)
            )
                    .querySingle();
            if (mataUang == null)
                new CurrencyDB(currency, currency, count++).save();
        }
        initCurrencyUnit(currencyUnit);
    }

    private String dollarCurrency;

    private void initCurrencyUnit(String[] currencyUnit) {

        List<SimpleTextModel> simpleTextModels = new ArrayList<>();
        for (String currency : currencyUnit) {
            simpleTextModels.add(new SimpleTextModel(currency));
        }
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_drop_down_resize);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.5),
                (int) (drawable.getIntrinsicHeight() * 0.5));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 0.1f, 0.1f);
        addProductCurrency.setCompoundDrawablesWithIntrinsicBounds(null, null, sd.getDrawable(), null);
        addProductCurrency.setItems(simpleTextModels);
        addProductCurrency.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<SimpleTextModel>() {
            @Override
            public void onItemSelectedListener(SimpleTextModel item, int selectedIndex) {

                boolean goldMerchant = SessionHandler.isGoldMerchant(AddProductFragment.this.getActivity());
                dollarCurrency = AddProductFragment.this.getActivity().getString(R.string.title_dollar);
                if (!goldMerchant && item.getLabel().equals(dollarCurrency)) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add("Anda Harus Berlangganan Gold Merchant");
                    showMessageError(strings);
                    addProductCurrency.setText(selectedCurrencyDesc);
                    return;
                }
                if (selectedCurrencyDesc != null && !item.getLabel().equals(selectedCurrencyDesc)) {
                    wholesaleLayout.removeAllWholesale();
                }
                selectedCurrency = selectedIndex;
                selectedCurrencyDesc = item.getLabel();
                reformatPrice();
            }
        });
        addProductCurrency.setLongClickable(false);

        selectedCurrency = 0;
        selectedCurrencyDesc = currencyUnit[selectedCurrency];

        addProductCurrency.setText(selectedCurrencyDesc);
    }

    private void reformatPrice() {
        String oldFormatPrice = addProductPrice.getText().toString();
        priceTextFormatter(oldFormatPrice);
    }

    public void getMyShopInfo() {
        addProduct.getMyShopInfo(getActivity());
    }

    @Override
    public void fetchEtalase() {
        addProduct.fetchEtalase(getActivity());
    }

    @Override
    public void clearAvailibilityOfShopNote() {
        addProduct.clearNoteAvailibility();
    }

    @Override
    public void showMessageError(List<String> errorMessages) {
        if (!checkCollectionNotNull(errorMessages))
            return;

        Snackbar.make(parentView, generateMessageError(getActivity(), errorMessages.toString()), Snackbar.LENGTH_LONG).show();
        showProgress(false);
//        Toast.makeText(AddProductFragment.this.getActivity(), errorMessages.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void initEtalaseAdapter(ArrayList<EtalaseModel> etalaseModels) {
        this.etalaseModels = etalaseModels;
        etalaseAdapter = new TextDeleteAdapter(displayEtalaseModels, ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE);
        etalaseAdapter.setSimpleTextModels(toSimpleTextEtalase(etalaseModels, 0));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addProductAddTo.setLayoutManager(layoutManager);
        addProductAddTo.setAdapter(etalaseAdapter);
    }

    @Override
    public void checkAvailibilityOfShopNote() {
        addProduct.checkNoteAvailibility(getActivity(), true);

    }

    @Override
    public void initReturnableSpinnerFromResource() {
        ArrayList<String> textToDisplay = new ArrayList<String>();
        for (String temp : getResources().getStringArray(R.array.return_policy)) {
            textToDisplay.add(temp);
        }
        initReturnableSpinner(textToDisplay);
    }

    @Override
    public void initReturnableSpinner(List<String> textToDisplay) {
        this.textToDisplay = textToDisplay;

    }


    @Override
    public void getReturnPolicyDetail(MyShopInfoModel.Info info) {
        myShopInfoModel = info;
        Log.d(TAG, messageTAG + " check return policy : " + returnPolicy);
        addProduct.getReturnPolicyDetail(getActivity(), myShopInfoModel, returnPolicy);
    }

    @Override
    public void saveReturnPolicy(GetShopNoteModel.ShopNoteModel returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    @Override
    public void saveReturnPolicyDetail(NoteDetailModel.Detail detail) {
        this.detail = detail;
    }

//    @Override
//    public ModalMultiSelectorCallback getmDeleteMode() {
//        return mDeleteMode;
//    }

    @OnFocusChange(R2.id.add_product_price)
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R2.id.add_product_price:
                Log.d(TAG, messageTAG + " price has focus " + hasFocus);
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                break;
        }
    }

    @OnTextChanged(R2.id.edittext_preorder)
    public void onTextChangedPreOrder(CharSequence s, int start, int before,
                                      int count) {
        Pair<Boolean, String> validate = VerificationUtils.validatePreOrder(getActivity(), s.toString());
        if (validate.getModel1()) {
            addProductEdittextPreorder.setError(null);
        } else {
            addProductEdittextPreorder.setError(validate.getModel2());
        }
    }

//    @OnTextChanged(R2.id.add_product_desc)
//    public void onTextChangedDescription(CharSequence s, int start, int before,
//                                      int count){
//        Pair<Boolean, String> validate = VerificationUtils.validateDescription(getActivity(), s.toString());
//        if(validate.getModel1()) {
//            addProductProductDescLayout.setErrorEnabled(false);
//            addProductProductDescLayout.setError(null);
//        }else {
//            addProductProductDescLayout.setErrorEnabled(true);
//            addProductProductDescLayout.setError(validate.getModel2());
//        }
//    }

    @OnTextChanged(R2.id.add_product_minimum_order)
    public void onTextChangedMinOrder(CharSequence s, int start, int before,
                                      int count) {
        validateMinOrder(s);
    }

    private void validateMinOrder(CharSequence s) {
        Pair<Boolean, String> validate = VerificationUtils.validateMininumOrder(getActivity(), s.toString());
        if (validate.getModel1()) {
            addProductMinimumOrderAlert.setErrorEnabled(false);
            addProductMinimumOrderAlert.setError(null);
        } else {
            addProductMinimumOrderAlert.setErrorEnabled(true);
            addProductMinimumOrderAlert.setError(validate.getModel2());
        }
    }

    @OnTextChanged(R2.id.add_product_weight)
    public void onTextChangedMinWeight(CharSequence s, int start, int before,
                                       int count) {
        validateProdWeight(s);
    }

    private void validateProdWeight(CharSequence s) {
        // This is old spinner way
//        Pair<Boolean, String> validate =
//                VerificationUtils.validateMinimumWeight(
//                        getActivity(),
//                        addProductWeightUnit.getSelectedItem().toString(),
//                        s.toString());
        Pair<Boolean, String> validate =
                VerificationUtils.validateMinimumWeight(
                        getActivity(),
                        selectedWeightUnitDesc,
                        s.toString());
        if (validate.getModel1()) {
            dismissWeightError();
        } else {
            addProductWeightAlert.setErrorEnabled(true);
            addProductWeightAlert.setError(validate.getModel2());
        }
    }

    private void dismissWeightError() {
        addProductWeightAlert.setError(null);
        addProductWeightAlert.setErrorEnabled(false);
    }

    @OnTextChanged(R2.id.add_product_product_name)
    public void onTextChangedProdName(CharSequence s, int start, int before,
                                      int count) {
        if (addProductProductName.isEnabled()) {
            validateProdName(s);
        }
    }

    private void validateProdName(CharSequence s) {
        Pair<Boolean, String> validate = VerificationUtils.validateProductName(getActivity(), s.toString());
        if (!validate.getModel1()) {
            addProductPRoductNameAlert.setErrorEnabled(true);
            addProductPRoductNameAlert.setError(validate.getModel2());
        } else {
            dismissErrorProductName();
        }
    }

    private void dismissErrorProductName() {
        addProductPRoductNameAlert.setError(null);
        addProductPRoductNameAlert.setErrorEnabled(false);
    }

    @OnTextChanged(R2.id.add_product_price)
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {

//        Log.d(TAG, messageTAG + s + " [] " + start + " [] " + before + " [] " + count + " [] " + addProductCurrency.getSelectedItem().toString());
        Log.d(TAG, messageTAG + s + " [] " + start + " [] " + before + " [] " + count + " [] " + selectedCurrencyDesc);
        priceTextFormatter(s.toString());
//        wholesaleLayout.removeAllWholesale();
    }

    private void priceTextFormatter(String s) {
        validateProdPrice(s);

        switch (selectedCurrencyDesc) {
            case "Rp":
                CurrencyFormatHelper.SetToRupiah(addProductPrice);
                break;
            case "US$":
                CurrencyFormatHelper.SetToDollar(addProductPrice);
                break;
        }
    }

    private void validateProdPrice(CharSequence s) {
//        Pair<Boolean, String> verif = VerificationUtils.validatePrice(getActivity(), addProductCurrency.getSelectedItem().toString(), s.toString());
        Pair<Boolean, String> verif = VerificationUtils.validatePrice(getActivity(), selectedCurrencyDesc, s.toString());
        if (!verif.getModel1()) {
            addProductPriceAlert.setErrorEnabled(true);
            addProductPriceAlert.setError(verif.getModel2());
        } else {
            dismissPriceError();
            Double price = addProductPrice.getText().toString().isEmpty()? 0 : Double.parseDouble(CurrencyFormatter.getRawString(addProductPrice.getText().toString()));
            int currency = (selectedCurrencyDesc == null || selectedCurrencyDesc.equals("Rp") ? PriceUtils.CURRENCY_RUPIAH : PriceUtils.CURRENCY_DOLLAR);
            wholesaleLayout.setupParams(price, currency, true);
        }
    }

    private void dismissPriceError() {
        addProductPriceAlert.setError(null);
        addProductPriceAlert.setErrorEnabled(false);
    }

    @Override
    public void fetchDepartmentChild(final int depId, final int level) {
        addProduct.fetchDepartmentChild(getActivity(), depId, level);
    }

    @Override
    public void processFetchDepartmentChild(DepartmentParentModel departmentParentModel, final int depId, int level) {

        addProductProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (addProductPRoductNameAlert.getError() == null)
                    addProduct.onNameChange(depId + "", addProductProductName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // save to db
        if (departmentParentModel != null)
            DbManagerImpl.getInstance().saveDepartment(departmentParentModel, depId);

        int levelTemp = -1;
        List<SimpleTextModel> simpleTextModels = toSimpleText(level, depId);
        switch (level) {
            case 2:// LEVEL 1
                categoryModels.clear();
                CategoryDB categoryDB = new Select().from(CategoryDB.class).where(
                        CategoryDB_Table.departmentId.is(depId)).querySingle();
                TextDeleteModel object = new TextDeleteModel(categoryDB.getNameCategory());
                object.setDepartmentId(categoryDB.getDepartmentId());
                categoryModels.add(object);

                List<SimpleTextModel> lvl1List = categoryAdapter.getSimpleTextModels(0);
                categoryAdapter.clearSimpleTextModels();
                categoryAdapter.setSimpleTextModels(lvl1List);


                if (simpleTextModels != null && simpleTextModels.size() > 0) {
                    categoryModels.add(new TextDeleteModel(true));
                    categoryAdapter.setSimpleTextModels(simpleTextModels);
                }
                catalogs = new ArrayList<>();
                addProductCatalogLayout.setVisibility(View.GONE);
                this.catalogs = null;
                break;
            case 3:// LEVEl 2
                TextDeleteModel lvl1 = categoryModels.get(0);
                categoryModels.clear();
                categoryDB = new Select().from(CategoryDB.class).where(CategoryDB_Table.departmentId.is(depId)).querySingle();
                categoryModels.add(lvl1);
                object = new TextDeleteModel(categoryDB.getNameCategory());
                object.setDepartmentId(categoryDB.getDepartmentId());
                categoryModels.add(object);

                lvl1List = categoryAdapter.getSimpleTextModels(0);
                List<SimpleTextModel> lvl2List = categoryAdapter.getSimpleTextModels(1);
                categoryAdapter.clearSimpleTextModels();
                categoryAdapter.setSimpleTextModels(lvl1List);
                categoryAdapter.setSimpleTextModels(lvl2List);
                if (simpleTextModels != null && simpleTextModels.size() > 0) {
                    categoryModels.add(new TextDeleteModel(true));
                    categoryAdapter.setSimpleTextModels(simpleTextModels);
                }

                if (addProductPRoductNameAlert.getError() == null)
                    fetchCatalog(depId + "", addProductProductName.getText().toString());
                break;
            case 4:// LEVEL 3
                lvl1 = categoryModels.get(0);
                TextDeleteModel lvl2 = categoryModels.get(1);
                categoryModels.clear();
                categoryDB = new Select().from(CategoryDB.class).where(CategoryDB_Table.departmentId.is(depId)).querySingle();
                categoryModels.add(lvl1);
                categoryModels.add(lvl2);
                object = new TextDeleteModel(categoryDB.getNameCategory());
                object.setDepartmentId(categoryDB.getDepartmentId());
                categoryModels.add(object);

                lvl1List = categoryAdapter.getSimpleTextModels(0);
                lvl2List = categoryAdapter.getSimpleTextModels(1);
                List<SimpleTextModel> lvl3List = categoryAdapter.getSimpleTextModels(2);
                categoryAdapter.clearSimpleTextModels();
                categoryAdapter.setSimpleTextModels(lvl1List);
                categoryAdapter.setSimpleTextModels(lvl2List);
                categoryAdapter.setSimpleTextModels(lvl3List);

                if (addProductPRoductNameAlert.getError() == null)
                    fetchCatalog(depId + "", addProductProductName.getText().toString());
                break;
        }
        categoryAdapter.notifyDataSetChanged();
    }

    public void fetchCatalog(String productDepId, String productName) {
        addProduct.fetchCatalog(getActivity(), productDepId, productName);
    }

    @Override
    public void saveCatalogs(ArrayList<CatalogDataModel.Catalog> catalogs) {
        Log.d(TAG, messageTAG + " : " + catalogs);
        if (!catalogs.isEmpty() && !catalogs.get(0).getCatalogName().equals(NO_CATALOG_OPTION)) {
            CatalogDataModel.Catalog noCatalogOption = new CatalogDataModel.Catalog();
            noCatalogOption.setCatalogName(NO_CATALOG_OPTION);
            catalogs.add(0, noCatalogOption);
        }
        this.catalogs = catalogs;
    }

    /**
     * set visibility of catalog to visible
     *
     * @param catalogs
     */
    @Override
    public void setToCatalogView(final ArrayList<CatalogDataModel.Catalog> catalogs) {
        addProductCatalog.clearSelection();
        if (catalogs != null && catalogs.size() > 0) {
            String[] temp = new String[catalogs.size() + 1];
            temp[0] = getActivity().getString(R.string.no_catalog_selected);
            for (int i = 0; i < catalogs.size(); i++) {
                temp[i + 1] = catalogs.get(i).getCatalogName();
            }
            addProductCatalogLayout.setVisibility(View.VISIBLE);
            addProductCatalog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductCatalog.showDialog(catalogs);
                }
            });

        } else {
            this.catalogs = new ArrayList<>();
            addProductCatalogLayout.setVisibility(View.GONE);

            this.catalogs = null;
        }
    }

    @Override
    public void setProductCatalog(int selection) {
        if (!checkNotNull(addProductCatalog))
            return;

        if (selection != 0 && selection != -1)
            addProductCatalog.setSelection(catalogs.get(selection));
        else
            addProductCatalog.clearSelection();
        addProductCatalog.dismissDialog();


    }

    @Override
    public void addEtalaseChooseText() {
        TextDeleteModel textDeleteModel = new TextDeleteModel(true);
        textDeleteModel.setCustomText(getActivity().getString(R.string.title_action_add_status));
        displayEtalaseModels.add(textDeleteModel);
    }

    @Override
    public void fetchProductData() {
        // for edit product
        if (productId != null && !productId.equals("XXX"))
            addProduct.fetchEditData(getActivity(), productId);

        // for modify data
        if (productDb != -1) {
            addProduct.getProductDb(getActivity(), productDb);
        }
    }

    public void fetchDepartmentParent() {
        addProduct.fetchDepartmentParent(getActivity());
    }

    public void addLevelZeroCategory() {
        //[START] T2409 [BUG] [Add Product] Crash if choose category on Kitkat OS
        AddProductFragment.this.categoryModels.add(new TextDeleteModel(true));
        //[END] T2409 [BUG] [Add Product] Crash if choose category on Kitkat OS

        categoryAdapter.setSimpleTextModels(toSimpleText(1, 0));
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnknown() {
        Snackbar.make(parentView, "Network Unknown Error!", Snackbar.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Network Unknown Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeout() {
        Snackbar.make(parentView, "Network Timeout Error!", Snackbar.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Network Timeout Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServerError() {
        Snackbar.make(parentView, "Network Internal Server Error!", Snackbar.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Network Internal Server Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBadRequest() {
        Snackbar.make(parentView, "Network Bad Request Error!", Snackbar.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Network Bad Request Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onForbidden() {
        Snackbar.make(parentView, "Network Forbidden Error!", Snackbar.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Network Forbidden Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public List<SimpleTextModel> toSimpleTextEtalase(List<EtalaseModel> etalaseModels, int... levelOrPosition) {
        List<SimpleTextModel> simpleTextModels = new ArrayList<>();
        if (levelOrPosition.length == 1) {
            int level = levelOrPosition[0];
            for (int i = 0; i < etalaseModels.size(); i++) {
                EtalaseModel mergePojo = etalaseModels.get(i);
                SimpleTextModel simpleTextModel = new SimpleTextModel(mergePojo.getText());
                simpleTextModel.setPosition(i);
                simpleTextModel.setLevel(LEVEL_ONE);
                simpleTextModels.add(simpleTextModel);
            }
        } else if (levelOrPosition.length == 2) {
            int level = levelOrPosition[0];
            int level1Pos = levelOrPosition[1];
            for (int i = 0; i < etalaseModels.get(level1Pos).getChilds().size(); i++) {
                String mergeChilds = etalaseModels.get(level1Pos).getChilds().get(i);
                SimpleTextModel simpleTextModel = new SimpleTextModel(mergeChilds);
                simpleTextModel.setPosition(i);
                simpleTextModel.setLevel(LEVEL_TWO);
                simpleTextModels.add(simpleTextModel);
            }
        }
        return simpleTextModels;
    }

    public static List<SimpleTextModel> toSimpleText(int level, int depId) {
        List<SimpleTextModel> simpleTextModels = new ArrayList<>();
        List<CategoryDB> categoryDBs = null;
        if (level == 1) {
            categoryDBs = new Select().from(CategoryDB.class).where(CategoryDB_Table.levelId.is(level)).queryList();
        } else {
            categoryDBs = new Select().from(CategoryDB.class).where(CategoryDB_Table.levelId.is(level))
                    .and(CategoryDB_Table.parentId.is(depId)).queryList();
        }
        if (categoryDBs == null)
            return null;

        for (int i = 0; i < categoryDBs.size(); i++) {
            CategoryDB categoryDB = categoryDBs.get(i);
            SimpleTextModel simpleTextModel = new SimpleTextModel(categoryDB.getNameCategory());
            //[START] set department id sebagai posisi karena ini berguna untuk query
            simpleTextModel.setPosition(categoryDB.getDepartmentId());
            //[END] set department id sebagai posisi karena ini berguna untuk query
            simpleTextModel.setLevel(level);
            simpleTextModels.add(simpleTextModel);
        }
        return simpleTextModels;
    }

    //[REMOVE] move onclick to its container
//    @OnClick(R2.id.add_product_submit_and_push)
//    public void pushProduct(){
//        pushAndCreateNewProduct(true);
//    }
//
//    @OnClick(R2.id.add_product_submit)
//    public void pushAndCreateNewProduct(){
//        pushAndCreateNewProduct(false);
//    }
    //[REMOVE] move onclick to its container


    @Override
    public void constructOriginalEditData() {
        producteditHelper.constructOriginalEditData(addProduct.getOriginalEditData());
    }

    public void editProduct(boolean isPush) {
        Pair<InputAddProductModel, TextDeleteModel> verif = verifProduct();
        if (verif == null)
            return;

        TextDeleteModel stockStatus = verif.getModel2();
        InputAddProductModel inputAddProductModel = verif.getModel1();

        int compare = 0;
        Map<String, String> isCatalogAndWholeSaleChange = null;
        if (checkNotNull(addProduct.getOriginalEditData())) {
            producteditHelper.constructOriginalEditData(addProduct.getOriginalEditData());
            Pair<Integer, Map<String, String>> integerMapPair = producteditHelper.compareWithEdittedData(inputAddProductModel, stockStatus);
            compare = integerMapPair.getModel1();
            isCatalogAndWholeSaleChange = integerMapPair.getModel2();
        } else {
            return;
        }

        if (!producteditHelper.isEdit(compare)) {
            Snackbar.make(parentView, "tidak ada perubahan", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (isPush) {
            ProductDb = InputAddProductModel.compileAllForEdit(verif.getModel1(), ProductDb, producteditHelper.editProductForm);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ProductService.PRODUCT_EDIT_PHOTOS, Parcels.wrap(producteditHelper.toParcelFormatForPhotos()));
            bundle.putLong(ProductService.PRODUCT_DATABASE_ID, ProductDb.getId());
            bundle.putString(ProductService.STOCK_STATUS, verif.getModel2().getText());
            bundle.putString(ProductService.SHOP_ID, SessionHandler.getShopID(getActivity()));
            bundle.putString(ProductService.PRODUCT_CHANGE_WHOLESALE, isCatalogAndWholeSaleChange.get(ProductService.PRODUCT_CHANGE_WHOLESALE));
            bundle.putString(ProductService.PRODUCT_CHANGE_CATALOG, isCatalogAndWholeSaleChange.get(ProductService.PRODUCT_CHANGE_CATALOG));
            bundle.putString(ProductService.PRODUCT_NAME, verif.getModel1().getProductName());

            //[START] send data to internet
            if (isPush && getActivity() != null && getActivity() instanceof DownloadResultSender) {
                ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.EDIT_PRODUCT, bundle);
            }
            //[END] send data to internet
        }
    }

    boolean isWithoutImage = false;

    public Pair<InputAddProductModel, TextDeleteModel> verifProduct() {
        InputAddProductModel inputAddProductModel = new InputAddProductModel();

        // T7247 [v2.1.4 (201040300)][Add Product]: App should not be crashed when the user tap on 'Save' button in offline mode.
        if (!checkCollectionNotNull(displayEtalaseModels)) {
            showMessageError(new ArrayList<String>() {{
                add(generateMessageError(AddProductFragment.this.getActivity(), "timeout this"));
            }});
            return null;
        }
        // T7247 [v2.1.4 (201040300)][Add Product]: App should not be crashed when the user tap on 'Save' button in offline mode.


        // 8. get etalase name
        TextDeleteModel stockStatus = displayEtalaseModels.get(0);
        inputAddProductModel.setStockStatus(stockStatus.getText());
        TextDeleteModel etalaseModel = displayEtalaseModels.get(displayEtalaseModels.size() - 1);
        long etalaseId = etalaseModel.getEtalaseId();
        if (addProductAddToNewEtalaseLayout.isExpanded()) {// jika tambah baru, override menjadi yang diketik
            Pair<Boolean, String> validate = VerificationUtils.validateNewEtalaseName(getActivity(), addProductAddToNewEtalase.getText().toString());
            if (validate.getModel1()) {
                addProductAddToNewEtalase.setError(null);
                addProductAddToNewEtalaseAlert.setError(null);
            } else {
                addProductAddToNewEtalase.setError(validate.getModel2());
                addProductAddToNewEtalaseAlert.setError(validate.getModel2());
            }
            if (!validate.getModel1()) {
                Snackbar.make(parentView, addProductAddToNewEtalaseAlert.getError(), Snackbar.LENGTH_LONG).show();
                return null;
            }

            EtalaseDB etalaseDB = DbManagerImpl.getInstance().getEtalase("-2");
            if (etalaseDB == null) {
                etalaseDB = new EtalaseDB(-2, addProductAddToNewEtalase.getText().toString(), -2);
                etalaseDB.save();
            } else {
                etalaseDB.setEtalaseName(addProductAddToNewEtalase.getText().toString());
                etalaseDB.update();
            }


            addProduct.clearEtalaseCache(getActivity());


            //[END] Override tambah baru dengan yang dimasukkan user

            inputAddProductModel.setEtalaseId(etalaseDB.getId());
            inputAddProductModel.setEtalaseName(addProductAddToNewEtalase.getText().toString());
        } else {
            Pair<Boolean, String> etalaseVerif = VerificationUtils.validateEtalase(getActivity(), displayEtalaseModels);
            if (!etalaseVerif.getModel1()) {
                Snackbar.make(parentView, etalaseVerif.getModel2(), Snackbar.LENGTH_LONG).show();
                return null;
            }
            inputAddProductModel.setEtalaseId(etalaseId);
            inputAddProductModel.setEtalaseName(etalaseModel.getText());
        }

        // 1. get images
        Pair<Boolean, String> validateImages = VerificationUtils.validateImages(getActivity(), photos);
        if (stockStatus.getText().equals(AddProductView.ETALASE_GUDANG) && !validateImages.getModel1()) {
            // do not store photos in here
            isWithoutImage = true;
        } else {
            if (!validateImages.getModel1()) {
                Snackbar.make(parentView, validateImages.getModel2(), Snackbar.LENGTH_LONG).show();
                return null;
            }
            inputAddProductModel.setImageModels(photos);
        }

        // 2. get name product
        if (addProductType != AddProductType.EDIT) {
            validateProdName(addProductProductName.getText().toString());
        }

        if (addProductPRoductNameAlert.isErrorEnabled()) {
            Snackbar.make(parentView, addProductPRoductNameAlert.getError(), Snackbar.LENGTH_LONG).show();
            return null;
        }
        inputAddProductModel.setProductName(addProductProductName.getText().toString());

        // 3. get kategori - ambil kategori terakhir
        Pair<Boolean, String> validateCategories = VerificationUtils.validateCategories(getActivity(), categoryModels);
        if (!validateCategories.getModel1()) {
            Snackbar.make(parentView, validateCategories.getModel2(), Snackbar.LENGTH_LONG).show();
            return null;
        }
        inputAddProductModel.setCategories(categoryModels);

        // 4. get harga dan mata uang - anggapan sekarang pakai rupiah
        validateProdPrice(addProductPrice.getText().toString());
        if (addProductPriceAlert.isErrorEnabled()) {
            Snackbar.make(parentView, addProductPriceAlert.getError(), Snackbar.LENGTH_LONG).show();
            return null;
        }
        // inputAddProductModel.setCurrencyUnit(addProductCurrency.getSelectedItem().toString());

        inputAddProductModel.setCurrencyUnit(selectedCurrencyDesc);
        String price = addProductPrice.getText().toString().replace(",", "");
        if (!inputAddProductModel.getCurrencyUnit().equals("US$")) {
            // remove cent
            price = price.replace(".", "");
        }
        inputAddProductModel.setPrice(price);


        // 5. get berat dan unit berat
        validateProdWeight(addProductWeight.getText().toString());
        if (addProductWeightAlert.isErrorEnabled()) {
            Snackbar.make(parentView, addProductWeightAlert.getError(), Snackbar.LENGTH_LONG).show();
            return null;
        }
        inputAddProductModel.setWeight(addProductWeight.getText().toString());
        inputAddProductModel.setWeightUnit(selectedWeightUnitDesc);
//        inputAddProductModel.setWeightUnit(addProductWeightUnit.getSelectedItem().toString());

        // 6. get minimal order
        validateMinOrder(addProductMinimumOrder.getText().toString());
        if (addProductMinimumOrderAlert.isErrorEnabled()) {
            Snackbar.make(parentView, addProductMinimumOrderAlert.getError(), Snackbar.LENGTH_LONG).show();
            return null;
        }
        inputAddProductModel.setMinimumOrder(Integer.parseInt(addProductMinimumOrder.getText().toString()));

        // 7. get harga grosir - compile ketika disini doang - kosongkan terlebih dahulu,
        Log.d(TAG, messageTAG + wholesaleLayout.getDatas());
        if(wholesaleLayout.checkIfErrorExist()){
            Snackbar.make(parentView, R.string.addproduct_wholesale_priceError, Snackbar.LENGTH_LONG).show();
            return null;
        }
        List<WholeSaleAdapterModel> datas = new ArrayList<>();
        for (WholesaleModel model : wholesaleLayout.getDatas()){
            WholeSaleAdapterModel adapterModel = new WholeSaleAdapterModel(model.getQtyOne(), model.getQtyTwo(), model.getQtyPrice());
            adapterModel.setbDid(DbManagerImpl.getInstance().addHargaGrosir(adapterModel));
            datas.add(adapterModel);
        }
        inputAddProductModel.setWholeSales(datas);

        // 9. get terima pengembalian
        inputAddProductModel.setReturnable(RETURNABLE_NO);
        // 10. get kondisi
        String[] array1 = getResources().getStringArray(R.array.condition);
        if (addProductCondition.getSelectedItem().toString().contains(array1[0])) {
            inputAddProductModel.setCondition(CONDITION_NEW);
        } else {
            inputAddProductModel.setCondition(CONDITION_OLD);
        }

        // 11. get asuransi
        String[] array = getResources().getStringArray(R.array.insurance);
        if (addProductInsurance.getSelectedItem().toString().contains(array[0])) {
            inputAddProductModel.setMustAsurance(MUST_ASURANCE_OPTIONAL);
        } else {
            inputAddProductModel.setMustAsurance(MUST_ASURANCE_YES);
        }

        // 12. deskripsi
//        if(addProductProductDescLayout.isErrorEnabled()){
//            Snackbar.make(parentView, addProductProductDescLayout.getError(),  Snackbar.LENGTH_LONG).show();
//            return null;
//        }
        String description;
        description = addProductDesc.getText().toString();
        inputAddProductModel.setDescription(description);

        // 13. preorder
        int preorder = 0;
        if (addProductEdittextPreorder.getError() == null) {
            preorder = Integer.parseInt(addProductEdittextPreorder.getText().toString());
        }
        inputAddProductModel.setPreOrder(preorder);

        // 14. catalog
        Long catalogId = -1L;
        String selectedCatalog = addProductCatalog.getSelectedItem();
        if (selectedCatalog.equals(getActivity().getString(R.string.no_catalog_selected)) || catalogs == null) {
            inputAddProductModel.setCatalog(-1);
        } else {
            for (CatalogDataModel.Catalog catalog : catalogs) {
                if (selectedCatalog.equals(catalog.getCatalogName())) {
                    catalogId = Long.parseLong(catalog.getCatalogId());
                }
            }
            inputAddProductModel.setCatalog(catalogId);
        }

        Log.d(TAG, messageTAG + inputAddProductModel);
//        Parcels.wrap(inputAddProductModel);

        return new Pair<>(inputAddProductModel, stockStatus);
    }

    /**
     * functional method for save or save with sending to the internet
     *
     * @param isPush true means send data to the internet, false means not send data to the internet
     */
    public void saveProduct(boolean isPush) {
        Pair<InputAddProductModel, TextDeleteModel> verif = verifProduct();
        if (verif == null)
            return;

        if (!isCreateNewActivity) {
            UnifyTracking.eventAddProduct();
            TrackingUtils.eventLoca(AppScreen.EVENT_ADDED_PRODUCT);
        } else {
            TrackingUtils.eventLoca(AppScreen.EVENT_ADDED_PRODUCT);
            UnifyTracking.eventAddProductMore();
        }

        String stockStatus = verif.getModel2().getText();
        boolean isWithoutImageAndStockEmpty = stockStatus.equals(AddProductView.ETALASE_GUDANG) && isWithoutImage;
        ProductDb = InputAddProductModel.compileAll(verif.getModel1(), ProductDb, isWithoutImageAndStockEmpty);
        Bundle bundle = new Bundle();
        bundle.putLong(ProductService.PRODUCT_DATABASE_ID, ProductDb.getId());
        if (getActivity() instanceof ProductSocMedActivity) {
            bundle.putInt(ProductService.PRODUCT_POSITION, ((ProductSocMedActivity) getActivity()).getCurrentFragmentPosition());
        }
        bundle.putString(ProductService.STOCK_STATUS, stockStatus);

        if (addProductShareContainer != null && addProductShareContainer.isFacebookAuth) {
            //[START] move to ProductShareFragment
            if (isPush && getActivity() != null && getActivity() instanceof DownloadResultSender) {
                if (isWithoutImageAndStockEmpty) {
                    bundle.putInt(ProductService.TYPE, ProductService.ADD_PRODUCT_WITHOUT_IMAGE);
                    ProductActivity.moveToProductShare(bundle, getActivity());
                } else {
                    bundle.putInt(ProductService.TYPE, ProductService.ADD_PRODUCT);
                    ProductActivity.moveToProductShare(bundle, getActivity());
                }
            }
            //[END] move to ProductShareFragment
        } else {
            //[START] send data to internet
            if (isPush && getActivity() != null && getActivity() instanceof DownloadResultSender) {
                if (isWithoutImageAndStockEmpty) {
                    ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.ADD_PRODUCT_WITHOUT_IMAGE, bundle);
                } else {
                    ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.ADD_PRODUCT, bundle);
                }
            }
            //[END] send data to internet
        }
    }

    @OnClick(R2.id.title_preorder)
    public void togglePreOder() {
        togglePreorder();
    }

    @OnClick(R2.id.add_product_tittle_wholesale)
    public void toggleWholeSale() {
//        if (!VerificationUtils.validatePrice(addProductCurrency.getSelectedItem().toString(), addProductPrice.getText().toString())) {
        Pair<Boolean, String> verif = VerificationUtils.validatePrice(getActivity(), selectedCurrencyDesc, addProductPrice.getText().toString(), "checkwholesale");
        if (!verif.getModel1()) {
            addProductPriceAlert.setError(verif.getModel2());
            addProductPrice.requestFocus();
        } else {
            toggleWholeSaleAfterVerify();
        }
    }

    private void togglePreorder() {
        addProductPreOderContent.toggle();
        addProductPreOderContent.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int w = addProductPreOderContent.getWidth();
                        int h = addProductPreOderContent.getHeight();
                        Log.v("W-H~WHOLESALE", w + "-" + h);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            addProductPreOderContent.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            addProductPreOderContent.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        if (checkNotNull(getActivity()) && getActivity() instanceof ProductSocMedActivity) {
//            ((ProductSocMedActivity) getActivity()).recalculateView();
        }
        if (addProductPreOderContent.isExpanded())
            addProductChevronPreorder.setImageResource(R.drawable.chevron_down);
        else
            addProductChevronPreorder.setImageResource(R.drawable.chevron_up);

    }

    private void toggleWholeSaleAfterVerify() {
        if (wholeSaleContainer != null) {
            wholeSaleContainer.toggle();
            wholeSaleContainer.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            int w = wholeSaleContainer.getWidth();
                            int h = wholeSaleContainer.getHeight();
                            Log.v("W-H~WHOLESALE", w + "-" + h);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                wholeSaleContainer.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                wholeSaleContainer.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    });
            if (checkNotNull(getActivity()) && getActivity() instanceof ProductSocMedActivity) {
//            ((ProductSocMedActivity) getActivity()).recalculateView();
            }
            if (wholeSaleContainer.isExpanded())
                chevron.setImageResource(R.drawable.chevron_down);
            else
                chevron.setImageResource(R.drawable.chevron_up);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        addProduct.onSuccessGetInboxTicketDetail(null);
        addProduct.unsubscribe();
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(final int type, Bundle data) {
        switch (type) {
            case ProductService.EDIT_PRODUCT:
            case ProductService.ADD_PRODUCT:
            case ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case ProductService.DELETE_PRODUCT:
                if (tkpdProgressDialog != null && tkpdProgressDialog.isProgress())
                    tkpdProgressDialog.dismiss();

                switch (addProductType) {
                    case ADD_FROM_GALLERY:
                    case ADD:
                    case ADD_MULTIPLE_FROM_GALERY:
                    case COPY:
                    case MODIFY:
                        final long productServerId = data.getLong(ProductService.PRODUCT_DATABASE_ID);
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProductDb = new Select().from(ProductDB.class).where(
                                        ProductDB_Table.productId.is((int) productServerId)
                                ).querySingle();
                                ProductDb.setPictureDBs(ProductDb.getImages());
                                ProductDb.setWholesalePriceDBs(ProductDb.getWholeSales());

                                //[START] move to manage product
                                if (getActivity() != null) {
                                    if (addProductShareContainer != null && addProductShareContainer.isFacebookAuth) {

                                        //[START] move to ProductShareFragment
                                        ShareData shareData = new ShareData();
                                        shareData.setName(ProductDb.getNameProd());

                                        if (CommonUtils.checkCollectionNotNull(ProductDb.getPictureDBs()))
                                            shareData.setImgUri(ProductDb.getPictureDBs().get(0).getPath());

                                        shareData.setUri(ProductDb.getProductUrl());
                                        shareData.setDescription(ProductDb.getDescProd());
                                        shareData.setPrice(ProductDb.getPriceProd() + "");
                                        ProductActivity.moveToProductShare(shareData, getActivity());

                                        //[END] move to ProductShareFragment
                                    }

                                    if (addProductShareContainer != null && addProductShareContainer.isTwitterAuth) {
                                        //[START] push to twiiter
                                        th = new TwitterHandler(AddProductFragment.this.getActivity());
                                        th.UpdateStatus(ProductDb.getProductUrl());
                                        //[END] push to twitter
                                    }

                                    if (addProductShareContainer != null && addProductShareContainer.isFacebookAuth) {
                                        return;
                                    }
                                    Intent intent;
                                    if (isCreateNewActivity) {
                                        intent = new Intent(getActivity(), ProductActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
                                        intent.putExtras(bundle);
                                        getActivity().startActivity(intent);
                                    } else {
                                        CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
                                        getActivity().startActivity(new Intent(getActivity(), ManageProduct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }

                                    getActivity().finish();
                                }
                            }
                        }, 100);//[END] move to manage product
                        break;
                    case ADD_FROM_SOCIAL_MEDIA:
                        int fragmentPosition = ((ProductSocMedActivity) getActivity()).getCurrentFragmentPosition();
                        if (addProductSocMedSubmitContainer != null) {
                            addProductSocMedSubmitContainer.turnOffButton();

                        }
                        noitfyCompleted(fragmentPosition);


//                        removeFragment(fragmentPosition);

                        break;
                    case EDIT:
                        Log.d(TAG, messageTAG + "berhasil masuk sini ");
                        getActivity().startActivity(new Intent(getActivity(), ManageProduct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    default:
                        throw new RuntimeException("please register your action type !!");

                }
                break;
        }
    }

    public void noitfyCompleted(int fragmentPosition) {
        if (getActivity() != null && getActivity() instanceof ProductSocMedPresenter) {
            ((ProductSocMedPresenter) getActivity()).noitfyCompleted(fragmentPosition);
        }
    }

    public void removeFragment(int fragmentPosition) {
        if (getActivity() != null && getActivity() instanceof ProductSocMedPresenter) {
            ((ProductSocMedPresenter) getActivity()).removeFragment(fragmentPosition);
        }
    }

    public void removeFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ProductService.PRODUCT_ID, productId);
        ((DownloadResultSender) getActivity()).sendDataToInternet(ProductService.DELETE_PRODUCT, bundle);
    }

    public void deleteProductDialog() {
        if (productId != null && !productId.equals("XXX")) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
            myAlertDialog.setMessage(getString(R.string.dialog_delete_product));

            myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    AddProductFragment.this.removeFragment();
                }

            });

            myAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            Dialog dialog = myAlertDialog.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        if (tkpdProgressDialog != null && tkpdProgressDialog.isProgress()) {
            tkpdProgressDialog.dismiss();
        }
        dismissReturnableDialog();
        Snackbar.make(parentView, (String) data[0], Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            tkpdProgressDialog.showDialog();
        } else {
            if (tkpdProgressDialog != null) {
                tkpdProgressDialog.dismiss();
            }
        }
    }


    public void showDialog() {
        if (getActivity() != null && getActivity() instanceof AddProductView) {
            ((ProductView) getActivity()).showTwitterDialog();
        }
    }

    public void onLoginTwitter() {
        if (addProductShareContainer != null) {
            addProductShareContainer.butTwitterToggle(true);
        }
    }

    public int countPicture() {
        int count = 0;
        for (ImageModel photo : photos) {
            if (photo.getPath() != null) {
                count++;
            }
        }
        Log.i(TAG, messageTAG + "number of active image : " + count);
        return count;
    }

    public void updateShopNote() {
        addProduct.checkNoteAvailibility(getActivity(), false);
    }
}

