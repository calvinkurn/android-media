package com.tokopedia.tkpd.myproduct.presenter;

import android.support.annotation.NonNull;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tokopedia.tkpd.myproduct.model.CatalogDataModel;
import com.tokopedia.tkpd.myproduct.model.DepartmentParentModel;
import com.tokopedia.tkpd.myproduct.model.GetShopNoteModel;
import com.tokopedia.tkpd.myproduct.model.ImageModel;
import com.tokopedia.tkpd.myproduct.model.MyShopInfoModel;
import com.tokopedia.tkpd.myproduct.model.NoteDetailModel;
import com.tokopedia.tkpd.myproduct.model.TextDeleteModel;
import com.tokopedia.tkpd.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.myproduct.model.EtalaseModel;
import com.tokopedia.tkpd.myproduct.model.SimpleTextModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 15/12/2015.
 */
public interface AddProductView extends BaseView{
    String ADD_PRODUCT_SOC_MED_DATA = "ADD_PRODUCT_SOC_MED_DATA";
    String ADD_PRODUCT_SOC_MED_POSITION = "ADD_PRODUCT_SOC_MED_POSITION";
    String ADD_PRODUCT_TYPE = "ADD_PRODUCT_TYPE";
    String FRAGMENT_TAG = "AddProductFragment";
    String FRAGMENT_EDIT_TAG = "AddProductFragment_EDIT";
    String FRAGMENT_SOC_MED_TAG = "AddProductFragment_SOC_MED";
    String TAG = "MNORMANSYAH";
    String messageTAG = "AddProductFragment : ";
    int LEVEL_ONE = 0;
    int LEVEL_TWO = 1;
    String SERVER_LANGUAGE = "new_add";
    String GOLANG_VALUE = "2";
    int RETURNABLE_YES = 1;
    int RETURNABLE_NO = 0;
    int CONDITION_NEW = 1;
    int CONDITION_OLD = 2;
    String ETALASE_GUDANG = "Stock Kosong";
    String ETALASE_ETALASE = "Stock Tersedia";
    String ETALASE_TAMBAH_BARU = "Tambah Etalase Baru";
    int ETALASE_ETALASE_INDEX = 1;
    String TAMBAH_ETALASE_BARU = "Tambah Etalase Baru";


    String SAVED_ALREADY_LOAD = "SAVED_ALREADY_LOAD";
    String SAVED_IMAGE_MODELS = "SAVED_IMAGE_MODELS";
    String SAVED_PRODUCT_NAME = "SAVED_PRODUCT_NAME";
    String SAVED_CATEGORIES = "SAVED_CATEGORIES";
    String SAVED_PREORDER = "SAVED_PREORDER";
    String SAVED_CURRENCY_UNIT = "SAVED_CURRENCY_UNIT";
    String SAVED_CURRENCY_DESC = "SAVED_CURRENCY_DESC";
    String SAVED_PRICE = "SAVED_PRICE";
    String SAVED_WEIGHT = "SAVED_WEIGHT";
    String SAVED_WEIGHT_UNIT = "SAVED_WEIGHT_UNIT";
    String SAVED_WEIGHT_DESC = "SAVED_WEIGHT_DESC";
    String SAVED_MIN_ORDER = "SAVED_MIN_ORDER";
    String SAVED_WHOLE_SALE = "SAVED_WHOLE_SALE";
    String SAVED_ETALASES = "SAVED_ETALASES";
    String SAVED_NEW_ETALASE = "SAVED_NEW_ETALASE";
    String SAVED_RETURN_POLICY = "SAVED_RETURN_POLICY";
    String SAVED_CONDITION = "SAVED_CONDITION";
    String SAVED_INSURANCE = "SAVED_INSURANCE";
    String SAVED_CONDITIONS = "SAVED_CONDITIONS";
    String SAVED_INSURANCES = "SAVED_INSURANCES";
    String SAVED_DESCRIPTION = "SAVED_DESCRIPTION";
    String SAVED_LOADING = "SAVED_LOADING";
    String SAVED_RETURN_POLICY_LIST = "SAVED_RETURN_POLICY_LIST";
    String SAVED_ETALASE_MODELS = "SAVED_ETALASE_MODELS";
    String SAVED_NEXT_CATEGORY_CHOOSER = "SAVED_NEXT_CATEGORY_CHOOSER";
    String SAVED_NEXT_ETALASE_CHOOSER = "SAVED_NEXT_ETALASE_CHOOSER";
    String SAVED_PRODUCT_DB_ID = "SAVED_PRODUCT_DB_ID";
    String SAVED_CATALOGS = "SAVED_CATALOGS";
    int MUST_ASURANCE_OPTIONAL = 0;
    int MUST_ASURANCE_YES = 1;
    String KEBIJAKAN_PENGEMBALIAN_PRODUK = "Kebijakan Pengembalian Produk";
    String PILIH = "Pilih";
    String TAMBAH = "Tambah";
    String CREATE_NEW_AFTER_FINISH = "CREATE_NEW_AFTER_FINISH";
    
    List<SimpleTextModel> toSimpleTextEtalase(List<EtalaseModel> etalaseModels, int... levelOrPosition);
    void addImageAfterSelect(String path, int position);
    void addEtalaseAfterSelect(SimpleTextModel model);
    void initVar();
    void fetchEtalase();
    void clearAvailibilityOfShopNote();
    void checkAvailibilityOfShopNote();
//    ModalMultiSelectorCallback getmDeleteMode();
    void showProgress(boolean show);
    void initCategoryAdapter(@NonNull ArrayList<TextDeleteModel> categoryModels);
    void initCategoryAdapter(List<List<SimpleTextModel>> textDeleteModels, @NonNull ArrayList<TextDeleteModel> categoryModels);
    void addLevelZeroCategory();
    void onUnknown();
    void onTimeout();
    void onServerError();
    void onBadRequest();
    void onForbidden();
    void initEtalaseAdapter(ArrayList<EtalaseModel> etalaseModels);
    void showMessageError(List<String> errorMessages);
    void initReturnableSpinner(List<String> textToDisplay);
    void initReturnableSpinnerFromResource();
    void getMyShopInfo();
    void getReturnPolicyDetail(MyShopInfoModel.Info info);
    void saveReturnPolicy(GetShopNoteModel.ShopNoteModel returnPolicy);
    void saveReturnPolicyDetail(NoteDetailModel.Detail detail);
    void processFetchDepartmentChild(DepartmentParentModel departmentParentModel, int depId, int level);
    void fetchDepartmentChild(final int depId, final int level);
    void saveCatalogs(ArrayList<CatalogDataModel.Catalog> catalogs);
    void setToCatalogView(ArrayList<CatalogDataModel.Catalog> catalogs);
    void addEtalaseChooseText();
    void fetchProductData();
    void initPhotoAdapter(List<ImageModel> datas);
    void setProductName(String productName);
    void setProductDesc(String productDescription);
    void setProductCurrencyUnit(String productCurrencyUnit);
    String getProductCurrencyUnit();
    void setProductPrice(String productPrice);
    void setWeightUnit(String weightUnit);
    void setProductWeight(String weight);
    void setProductMinOrder(String minOrder);
    void initWholeSaleAdapter(ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels);
    void initWholeSaleAdapter();
    void setProductEtalase(boolean isGudang, String productEtalaseId);
    void setProductReturnable(boolean returnable);
    void setProductCondition(boolean isNew);
    void setProductInsurance(boolean isInsurance);
    void setProductCatalog(int selection);
    void setProductPreOrder(String preOrderDay);
    void disableProductNameEdit();
    void constructOriginalEditData();
    void dismissDialog();

    void enableProductNameEdit();
}
