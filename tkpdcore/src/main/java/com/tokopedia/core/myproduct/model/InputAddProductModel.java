package com.tokopedia.core.myproduct.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CurrencyDB;
import com.tokopedia.core.database.model.CurrencyDB_Table;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.EtalaseDB_Table;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.PictureDB_Table;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.ReturnableDB;
import com.tokopedia.core.database.model.ReturnableDB_Table;
import com.tokopedia.core.database.model.StockStatusDB;
import com.tokopedia.core.database.model.StockStatusDB_Table;
import com.tokopedia.core.database.model.WeightUnitDB;
import com.tokopedia.core.database.model.WeightUnitDB_Table;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.database.model.WholesalePriceDB_Table;
import com.tokopedia.core.myproduct.model.editProductForm.EditProductForm;
import com.tokopedia.core.myproduct.model.editproduct.EditProductModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.utils.ProductEditHelper;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;
import static com.tokopedia.core.newgallery.presenter.ImageGalleryImpl.Pair;

/**
 * Created by m.normansyah on 28/12/2015.
 * product catalog - currently not supported.
 * click_name ( ? ) --> perlu ditanya ke server
 * duplicate ( ? ) --> perlu ditanya ke server
 */
public class InputAddProductModel {
    public static final String TAG = "STUART";
    List<ImageModel> imageModels;
    String productName;
    List<TextDeleteModel> categories;
    String currencyUnit;
    String price;
    String weight;
    String weightUnit;
    int minimumOrder;
    List<WholeSaleAdapterModel> wholeSales;
    long etalaseId;
    String etalaseName;
    int mustAsurance;
    int condition;
    int returnable;
    String description;
    int preOrder;
    long catalog;
    int position;
    String stockStatus;
    ArrayList<ProductEditHelper.ProductEditImage> productEditImages;
    String shopId;

    String productChangeCatalog;
    String productChangePhoto;
    String productChangeWholeSale;

    
    static final int product_upload_to = 1;

    //[START] setter and getter

    public String getProductChangeWholeSale() {
        return productChangeWholeSale;
    }

    public void setProductChangeWholeSale(String productChangeWholeSale) {
        this.productChangeWholeSale = productChangeWholeSale;
    }

    public String getProductChangeCatalog() {
        return productChangeCatalog;
    }

    public void setProductChangeCatalog(String productChangeCatalog) {
        this.productChangeCatalog = productChangeCatalog;
    }

    public String getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(String productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public ArrayList<ProductEditHelper.ProductEditImage> getProductEditImages() {
        return productEditImages;
    }

    public void setProductEditImages(ArrayList<ProductEditHelper.ProductEditImage> productEditImages) {
        this.productEditImages = productEditImages;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getCatalog() {
        return catalog;
    }

    public void setCatalog(long catalog) {
        this.catalog = catalog;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public List<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<TextDeleteModel> getCategories() {
        return categories;
    }

    public void setCategories(List<TextDeleteModel> categories) {
        this.categories = categories;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public List<WholeSaleAdapterModel> getWholeSales() {
        return wholeSales;
    }

    public void setWholeSales(List<WholeSaleAdapterModel> wholeSales) {
        this.wholeSales = wholeSales;
    }

    public List<Pair<PictureDB, UploadProductImageData>> getUploadProductImageData() {
        return uploadProductImageData;
    }

    public void setUploadProductImageData(List<Pair<PictureDB, UploadProductImageData>> uploadProductImageData) {
        this.uploadProductImageData = uploadProductImageData;
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public int getMustAsurance() {
        return mustAsurance;
    }

    public void setMustAsurance(int mustAsurance) {
        this.mustAsurance = mustAsurance;
    }

    public int getReturnable() {
        return returnable;
    }

    public void setReturnable(int returnable) {
        this.returnable = returnable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static int getProduct_upload_to() {
        return product_upload_to;
    }

    public GenerateHostModel getGenerateHostModel() {
        return generateHostModel;
    }

    public void setGenerateHostModel(GenerateHostModel generateHostModel) {
        this.generateHostModel = generateHostModel;
    }

    public int getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(int preOrder) {
        this.preOrder = preOrder;
    }

    //[END] setter and getter

    /**
     * compile input from view to match db data
     * @param inputAddProductModel input that needed for db
     * @return product
     */
    @Deprecated
    public static ProductDB compileAll(InputAddProductModel inputAddProductModel){
        // 1
        WeightUnitDB weightUnitDB =
                new Select()
                .from(WeightUnitDB.class)
                .where(WeightUnitDB_Table.descWeight.eq(inputAddProductModel.getWeightUnit()))
                .querySingle();

        // 2
        CurrencyDB mataUang =
                new Select()
                .from(CurrencyDB.class)
                .where(CurrencyDB_Table.descCurr.eq(inputAddProductModel.getCurrencyUnit()))
                .querySingle();

        // 3
        EtalaseDB etalaseDB = new Select().from(EtalaseDB.class).where(EtalaseDB_Table.Id.is(inputAddProductModel.getEtalaseId()))
                .querySingle();

        // 4
        TextDeleteModel category = inputAddProductModel.getCategories().get(inputAddProductModel.getCategories().size() - 1);
        CategoryDB categoryDB = DbManagerImpl.getInstance().getKategoriByDepId(category.getDepartmentId());

        // 5
        ReturnableDB returnableDB = null;
        if(inputAddProductModel.getReturnable()==1){
            returnableDB = new Select().from(ReturnableDB.class)
                    .where(ReturnableDB_Table.note_title.eq(ReturnableDB.PENGEMBALIAN))
                    .querySingle();;
        }


        ProductDB produk = new ProductDB();
        produk.setWeightUnitDB(weightUnitDB);
        produk.setWeightProd(Integer.parseInt(inputAddProductModel.getWeight()));
        produk.setDescProd(inputAddProductModel.getDescription());
        produk.setAssuranceProd(inputAddProductModel.getMustAsurance());
        produk.setPriceProd(Double.parseDouble(inputAddProductModel.getPrice()));
        produk.setUnitCurrencyDB(mataUang);
        produk.setReturnableProd(inputAddProductModel.getReturnable());
        produk.setEtalaseDB(etalaseDB);
        produk.setConditionProd(inputAddProductModel.getCondition());
        produk.setMinOrderProd(inputAddProductModel.getMinimumOrder());
        produk.setNameProd(inputAddProductModel.getProductName());
        produk.setCategoryDB(categoryDB);
        produk.setKebijakanReturnableDB(returnableDB);
        produk.save();

        for(ImageModel imageModel : inputAddProductModel.getImageModels()){
            PictureDB pictureDB = new Select().from(PictureDB.class)
                    .where(PictureDB_Table.Id.is(imageModel.getDbId()))
                    .querySingle();
            pictureDB.setProductDB(produk);
            pictureDB.save();
        }

        for(WholeSaleAdapterModel wholeSaleAdapterModel : inputAddProductModel.getWholeSales()){
            WholesalePriceDB wholesalePriceDB = new WholesalePriceDB(
                    (int)wholeSaleAdapterModel.getQuantityOne(),
                    (int)wholeSaleAdapterModel.getQuantityTwo(),
                    wholeSaleAdapterModel.getWholeSalePrice()
            );
            wholesalePriceDB.setProduk(produk);
            wholesalePriceDB.save();
        }

        Log.d("MNORMANSYAH", PictureDB.class.getSimpleName()+" : "+ produk.getImages());
        Log.d("MNORMANSYAH", WholesalePriceDB.class.getSimpleName()+" : "+produk.getWholeSales());
        return produk;
    }

    public static ProductDB compileAllForEdit(InputAddProductModel inputAddProductModel,
                                              ProductDB produk, EditProductForm.Data editProductForm){

        // 1
        WeightUnitDB weightUnitDB =
                new Select()
                        .from(WeightUnitDB.class)
                        .where(WeightUnitDB_Table.descWeight.eq(inputAddProductModel.getWeightUnit()))
                        .querySingle();

        // 2
        CurrencyDB mataUang =
                new Select()
                        .from(CurrencyDB.class)
                        .where(CurrencyDB_Table.descCurr.eq(inputAddProductModel.getCurrencyUnit()))
                        .querySingle();

        // 3
        EtalaseDB etalaseDB = new Select().from(EtalaseDB.class).where(EtalaseDB_Table.Id.is(inputAddProductModel.getEtalaseId()))
                .querySingle();

        // 4
        TextDeleteModel category = inputAddProductModel.getCategories().get(inputAddProductModel.getCategories().size() - 1);
        CategoryDB categoryDB = DbManagerImpl.getInstance().getKategoriByDepId(category.getDepartmentId());

        // 5
        ReturnableDB returnableDB = null;
        if(inputAddProductModel.getReturnable()==1){
            returnableDB = new Select().from(ReturnableDB.class)
                    .where(ReturnableDB_Table.note_title.eq(ReturnableDB.PENGEMBALIAN))
                    .querySingle();;
        }

        // 6
        StockStatusDB stockStatusDB = getStockStatusDB(inputAddProductModel);

        boolean isnew = false;
        if(produk==null) {
            produk = new ProductDB();
            isnew = true;
        }
        produk.setWeightUnitDB(weightUnitDB);
        produk.setWeightProd(Integer.parseInt(inputAddProductModel.getWeight()));
        produk.setDescProd(inputAddProductModel.getDescription());
        produk.setAssuranceProd(inputAddProductModel.getMustAsurance());
        produk.setPriceProd(Double.parseDouble(inputAddProductModel.getPrice()));
        produk.setUnitCurrencyDB(mataUang);
        produk.setReturnableProd(inputAddProductModel.getReturnable());
        produk.setEtalaseDB(etalaseDB);
        produk.setConditionProd(inputAddProductModel.getCondition());
        produk.setMinOrderProd(inputAddProductModel.getMinimumOrder());
        produk.setNameProd(inputAddProductModel.getProductName());
        produk.setCategoryDB(categoryDB);
        produk.setKebijakanReturnableDB(returnableDB);
        produk.setProductPreOrder(inputAddProductModel.getPreOrder());
        produk.setCatalogid(inputAddProductModel.getCatalog());
        produk.setProductId(Integer.parseInt(editProductForm.getProduct().getProductId()));// product id
        produk.setProductUrl(editProductForm.getProduct().getProductUrl());// product url
        produk.setStockStatusDB(stockStatusDB);
        if (isnew) {
            produk.save();
        } else {
            produk.update();
        }

        if(checkCollectionNotNull(inputAddProductModel.getImageModels())){
            for (ImageModel imageModel : inputAddProductModel.getImageModels()) {
                if (imageModel.getDbId() != 0) {
                    PictureDB pictureDB = DbManagerImpl.getInstance().getGambarById(imageModel.getDbId());
                    if (pictureDB != null) {
                        pictureDB.setProductDB(produk);
                        pictureDB.save();
                    }
                }
            }
        }

        for(WholeSaleAdapterModel wholeSaleAdapterModel : inputAddProductModel.getWholeSales()){
            WholesalePriceDB wholesalePriceDB = new Select()
                    .from(WholesalePriceDB.class)
                    .where(WholesalePriceDB_Table.Id.is(wholeSaleAdapterModel.getbDid()))
                    .querySingle();
            if(checkNotNull(wholesalePriceDB)) {
                wholesalePriceDB.setMin((int) wholeSaleAdapterModel.getQuantityOne());
                wholesalePriceDB.setMax((int) wholeSaleAdapterModel.getQuantityTwo());
                wholesalePriceDB.setPriceWholesale(wholeSaleAdapterModel.getWholeSalePrice());
                wholesalePriceDB.setProduk(produk);
                wholesalePriceDB.save();
            }
        }

        Log.d("MNORMANSYAH", PictureDB.class.getSimpleName() + " : " + produk.getImages());
        Log.d("MNORMANSYAH", WholesalePriceDB.class.getSimpleName() + " : " + produk.getWholeSales());


        return produk;
    }

    @NonNull
    public static StockStatusDB getStockStatusDB(InputAddProductModel inputAddProductModel) {
        StockStatusDB stockStatusDB = new Select().from(StockStatusDB.class)
                .where(StockStatusDB_Table.stockDetail.eq(inputAddProductModel.getStockStatus()))
                .querySingle();

        if(stockStatusDB == null){
            stockStatusDB = new StockStatusDB(inputAddProductModel.getStockStatus());
            stockStatusDB.save();
        }
        return stockStatusDB;
    }

    public static ProductDB compileAll(InputAddProductModel inputAddProductModel,
                                       ProductDB produk, boolean isWithoutImage){
        // 1
        WeightUnitDB weightUnitDB =
                new Select()
                        .from(WeightUnitDB.class)
                        .where(WeightUnitDB_Table.descWeight.eq(inputAddProductModel.getWeightUnit()))
                        .querySingle();

        // 2
        CurrencyDB mataUang =
                new Select()
                        .from(CurrencyDB.class)
                        .where(CurrencyDB_Table.descCurr.eq(inputAddProductModel.getCurrencyUnit()))
                        .querySingle();

        // 3
        EtalaseDB etalaseDB = new Select().from(EtalaseDB.class).where(EtalaseDB_Table.Id.is(inputAddProductModel.getEtalaseId()))
                .querySingle();

        // 4
        TextDeleteModel category = inputAddProductModel.getCategories().get(inputAddProductModel.getCategories().size() - 1);
        CategoryDB categoryDB = DbManagerImpl.getInstance().getKategoriByDepId(category.getDepartmentId());

        // 5
        ReturnableDB returnableDB = null;
        if(inputAddProductModel.getReturnable()==1){
            returnableDB = new Select().from(ReturnableDB.class)
                    .where(ReturnableDB_Table.note_title.eq(ReturnableDB.PENGEMBALIAN))
                    .querySingle();
        }

        StockStatusDB stockStatusDB = new Select().from(StockStatusDB.class)
                .where(StockStatusDB_Table.stockDetail.eq(inputAddProductModel.getStockStatus()))
                .querySingle();

        if(stockStatusDB == null){
            stockStatusDB = new StockStatusDB(inputAddProductModel.getStockStatus());
            stockStatusDB.save();
        }

        if(produk==null)
            produk = new ProductDB();
        produk.setWeightUnitDB(weightUnitDB);
        produk.setWeightProd(Integer.parseInt(inputAddProductModel.getWeight()));
        produk.setDescProd(inputAddProductModel.getDescription());
        produk.setAssuranceProd(inputAddProductModel.getMustAsurance());
        produk.setPriceProd(Double.parseDouble(inputAddProductModel.getPrice()));
        produk.setUnitCurrencyDB(mataUang);
        produk.setReturnableProd(inputAddProductModel.getReturnable());
        produk.setEtalaseDB(etalaseDB);
        produk.setConditionProd(inputAddProductModel.getCondition());
        produk.setMinOrderProd(inputAddProductModel.getMinimumOrder());
        produk.setNameProd(inputAddProductModel.getProductName());
        produk.setCategoryDB(categoryDB);
        produk.setKebijakanReturnableDB(returnableDB);
        produk.setProductPreOrder(inputAddProductModel.getPreOrder());
        produk.setCatalogid(inputAddProductModel.getCatalog());
        produk.setStockStatusDB(stockStatusDB);
        produk.save();

        if(!isWithoutImage) {
            Log.d(TAG,"Product with image");
            for (ImageModel imageModel : inputAddProductModel.getImageModels()) {
                if (imageModel.getDbId() != 0) {
                    PictureDB pictureDB = new Select().from(PictureDB.class).where(PictureDB_Table.Id.is(imageModel.getDbId()))
                            .querySingle();
                    if (pictureDB != null) {
                        pictureDB.setProductDB(produk);
                        pictureDB.save();
                    }
                }
            }
            Log.d("MNORMANSYAH", PictureDB.class.getSimpleName() + " : " + produk.getImages());
        }

        for(WholeSaleAdapterModel wholeSaleAdapterModel : inputAddProductModel.getWholeSales()){
            WholesalePriceDB wholesalePriceDB = new Select()
                    .from(WholesalePriceDB.class)
                    .where(WholesalePriceDB_Table.Id.is(wholeSaleAdapterModel.getbDid()))
                    .querySingle();
            wholesalePriceDB.setMin((int)wholeSaleAdapterModel.getQuantityOne());
            wholesalePriceDB.setMax((int)wholeSaleAdapterModel.getQuantityTwo());
            wholesalePriceDB.setPriceWholesale(Double.valueOf(wholeSaleAdapterModel.getWholeSalePrice()));
            wholesalePriceDB.setProduk(produk);
            wholesalePriceDB.save();
        }

        Log.d("MNORMANSYAH", WholesalePriceDB.class.getSimpleName() + " : " + produk.getWholeSales());
        return produk;
    }

    public static ProductDB compileAll(InputAddProductModel inputAddProductModel,
                                       ProductDB produk){
        return compileAll(inputAddProductModel, produk, false);
    }


    // use below for processing
    
    ProductDB produk;
    
    NetworkCalculator networkCalculator;
    GenerateHostModel generateHostModel;
    List<Pair<PictureDB, UploadProductImageData>> uploadProductImageData;
    GeneratedHostActApi generatedHostActApi;
    ProductValidationModel productValidationModel;
    AddProductPictureModel addProductPictureModel;
    ProductSubmitModel productSubmitModel;
    Bitmap bitmap;
    EditProductModel.Data editProductData;
    List<Pair<Integer, Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>>> newPairs;

    public List<Pair<Integer, Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>>> getNewPairs() {
        return newPairs;
    }

    public void setNewPairs(List<Pair<Integer, Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>>> newPairs) {
        this.newPairs = newPairs;
    }

    public EditProductModel.Data getEditProductData() {
        return editProductData;
    }

    public void setEditProductData(EditProductModel.Data editProductData) {
        this.editProductData = editProductData;
    }

    @Deprecated
    public List<ImageGalleryImpl.Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>> pairs;

    public List<ImageGalleryImpl.Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>> getPairs() {
        return pairs;
    }

    public void setPairs(List<ImageGalleryImpl.Pair<EditProductPictureModel, com.tokopedia.core.myproduct.service.ProductService.EditProductInputModel>> pairs) {
        this.pairs = pairs;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ProductSubmitModel getProductSubmitModel() {
        return productSubmitModel;
    }

    public void setProductSubmitModel(ProductSubmitModel productSubmitModel) {
        this.productSubmitModel = productSubmitModel;
    }

    public AddProductPictureModel getAddProductPictureModel() {
        return addProductPictureModel;
    }

    public void setAddProductPictureModel(AddProductPictureModel addProductPictureModel) {
        this.addProductPictureModel = addProductPictureModel;
    }

    public ProductValidationModel getProductValidationModel() {
        return productValidationModel;
    }

    public void setProductValidationModel(ProductValidationModel productValidationModel) {
        this.productValidationModel = productValidationModel;
    }

    public GeneratedHostActApi getGeneratedHostActApi() {
        return generatedHostActApi;
    }

    public void setGeneratedHostActApi(GeneratedHostActApi generatedHostActApi) {
        this.generatedHostActApi = generatedHostActApi;
    }

    public ProductDB getProduk() {
        return produk;
    }

    public void setProduk(ProductDB produk) {
        this.produk = produk;
    }

    public NetworkCalculator getNetworkCalculator() {
        return networkCalculator;
    }

    public void setNetworkCalculator(NetworkCalculator networkCalculator) {
        this.networkCalculator = networkCalculator;
    }
}
