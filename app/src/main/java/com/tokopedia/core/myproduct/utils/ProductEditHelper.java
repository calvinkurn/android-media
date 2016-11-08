package com.tokopedia.core.myproduct.utils;

import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.InputAddProductModel;
import com.tokopedia.core.myproduct.model.TextDeleteModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.core.myproduct.model.editProductForm.EditProductForm;
import com.tokopedia.core.myproduct.presenter.AddProductView;
import com.tokopedia.core.myproduct.presenter.NetworkInteractor;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;
import static com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;

/**
 * Created by noiz354 on 5/27/16.
 */
public class ProductEditHelper {

    public static final int IMAGE_STILL = 0;
    public static final int IMAGE_CHANGED = 1;
    public static final int IMAGE_REMOVED = 2;
    public static final int IMAGE_ADDED = 3;

    public static final String IMAGE_ONE = "IMAGE_ONE";
    public static final String IMAGE_TWO = "IMAGE_TWO";
    public static final String IMAGE_THREE = "IMAGE_THREE";
    public static final String IMAGE_FOUR = "IMAGE_FOUR";
    public static final String IMAGE_FIVE = "IMAGE_FIVE";



    Map<String, Queue<Pair<Integer, ImageModel>>> photosEdit = new HashMap<>();
    Map<String, String> textEdits = new HashMap<>();


    List<ImageModel> imageModelList;// original image Model
    public ProductDetailData productDetailData;
    CatalogDataModel catalogDataModel;
    private EditProductForm.Data editProductForm;

    public ArrayList<ProductEditImage> toParcelFormatForPhotos(){
        ArrayList<ProductEditImage> productEditImages = new ArrayList<>();
        Set<String> photoKeySet = photosEdit.keySet();
        for(Iterator<String> iterator = photoKeySet.iterator(); iterator.hasNext();){
            String key = iterator.next();
            Queue<Pair<Integer, ImageModel>> pairs = photosEdit.get(key);
            for(Pair<Integer, ImageModel> data : pairs){
                if(checkNotNull(data)){
                    ProductEditImage p = new ProductEditImage();
                    int location = fromStringKey(key);
                    if(location > imageModelList.size()-1 && location < 0){
                        p.oriImageModel = null;
                    }else {
                        p.oriImageModel = imageModelList.get(location);
                        p.position = location;
                    }
                    p.updateImageModel = data.getModel2();
                    p.imageStatus = data.getModel1();

                    productEditImages.add(p);
                }
            }
        }
        return productEditImages;
    }

    public void addImage(ImageModel imageModel, int position){
        if(imageModel.isDefault())
            return;


        Pair<Integer, ImageModel> pairImages = new Pair<>(IMAGE_ADDED, new ImageModel(imageModel));
        putAtDataStructure(position, pairImages);
    }

    @Deprecated
    public void updateImage(ImageModel imageModel, int position){
        if(imageModel.isDefault() || imageModel.getGambar().getPictureId() == PictureDB.NOT_SYNC_TO_SERVER)
            return;


        Pair<Integer, ImageModel> pairImages = new Pair<>(IMAGE_CHANGED, new ImageModel(imageModel));
        putAtDataStructure(position, pairImages);
    }

    public int fromStringKey(String key){
        int result = -1;
        switch (key){
            case IMAGE_ONE:
                result = 0;
                break;
            case IMAGE_TWO:
                result = 1;
                break;
            case IMAGE_THREE:
                result = 2;
                break;
            case IMAGE_FOUR:
                result = 3;
                break;
            case IMAGE_FIVE:
                result = 4;
                break;
        }
        return result;
    }

    public void deleteImage(ImageModel imageModel, int position){
        if(imageModel.isDefault() || imageModel.getGambar().getPictureId() == PictureDB.NOT_SYNC_TO_SERVER)
            return;

        Pair<Integer, ImageModel> pairImages = new Pair<>(IMAGE_REMOVED, new ImageModel(imageModel));
        putAtDataStructure(position, pairImages);
    }

    @Deprecated
    public void deleteImage(int position){
        ImageModel oriModel = imageModelList.get(position);
        if(oriModel.isDefault()) {
            return;
        }
        Pair<Integer, ImageModel> pairImages = new Pair<>(IMAGE_REMOVED, new ImageModel(oriModel));
        putAtDataStructure(position, pairImages);
    }

    private void putAtDataStructure(int position, Pair<Integer, ImageModel> pairImages) {
        switch (position){
            case 0:
                Queue<Pair<Integer, ImageModel>> pairs = photosEdit.get(IMAGE_ONE);
                if(!checkCollectionNotNull(pairs)){
                    pairs = new LinkedList<>();
                }
                pairs.add(pairImages);
                photosEdit.put(IMAGE_ONE, pairs);
                break;
            case 1:
                pairs = photosEdit.get(IMAGE_TWO);
                if(!checkCollectionNotNull(pairs)){
                    pairs = new LinkedList<>();
                }
                pairs.add(pairImages);
                photosEdit.put(IMAGE_TWO, pairs);
                break;
            case 2:
                pairs = photosEdit.get(IMAGE_THREE);
                if(!checkCollectionNotNull(pairs)){
                    pairs = new LinkedList<>();
                }
                pairs.add(pairImages);
                photosEdit.put(IMAGE_THREE, pairs);
                break;
            case 3:
                pairs = photosEdit.get(IMAGE_FOUR);
                if(!checkCollectionNotNull(pairs)){
                    pairs = new LinkedList<>();
                }
                pairs.add(pairImages);
                photosEdit.put(IMAGE_FOUR, pairs);
                break;
            case 4:
                pairs = photosEdit.get(IMAGE_FIVE);
                if(!checkCollectionNotNull(pairs)){
                    pairs = new LinkedList<>();
                }
                pairs.add(pairImages);
                photosEdit.put(IMAGE_FIVE, pairs);
                break;
        }
    }

    public boolean isEdit(int compare){
        int count = 0;
        Set<String> key = photosEdit.keySet();
        for(Iterator<String> iterator = key.iterator(); iterator.hasNext(); ){
            Queue<Pair<Integer, ImageModel>> pairs = photosEdit.get(iterator.next());
            for(Pair<Integer, ImageModel> integerImageModelPair : pairs){
                switch(integerImageModelPair.getModel1()){
                    case IMAGE_CHANGED:
                    case IMAGE_REMOVED:
                        count++;
                        break;
                }
            }
        }

        count = compare + compare;
        return count >= 1;
    }

    public void constructOriginalEditData(Map<String, Object> originalData){
        Object object = originalData.get(NetworkInteractor.IMAGE_MODEL_DOWNLOADS);
        if(object != null ) {// && object instanceof List<?>
            imageModelList = (List<ImageModel>) object;
            imageModelList = new LinkedList<>(imageModelList);
        }

        object  = originalData.get(NetworkInteractor.PRODUCT_DETAIL_DATA);
        if(object != null && object instanceof ProductDetailData) {
            productDetailData = (ProductDetailData) object;
        }

        object  = originalData.get(NetworkInteractor.CATALOG_MODEL_EDIT);
        if(object != null && object instanceof CatalogDataModel){
            catalogDataModel = (CatalogDataModel)object;
        }

        object = originalData.get(NetworkInteractor.EDIT_PRODUCT_FORM);
        if(checkNotNull(object) && object instanceof EditProductForm) {
            editProductForm = ((EditProductForm) object).getData();
        }
    }

    public Pair<Integer, Map<String, String>> compareWithEdittedData(InputAddProductModel inputAddProductModel, TextDeleteModel stockStatus){
        int count = 0;
        // 1. jika nama tidak sama
        if(!inputAddProductModel.getProductName().equals(productDetailData.getInfo().getProductName())){
            count++;
        }

        // 2. kategori berubah
        TextDeleteModel categoryEdit = inputAddProductModel.getCategories().get(inputAddProductModel.getCategories().size() - 1);
        CategoryDB categoryDBEdit = DbManagerImpl.getInstance().getKategoriByDepId(categoryEdit.getDepartmentId());

        int lastIndex = productDetailData.getBreadcrumb().size()-1;
        int kategoriOri = Integer.parseInt(productDetailData.getBreadcrumb().get(lastIndex).getDepartmentId());
        CategoryDB categoryDBOld = DbManagerImpl.getInstance().getKategoriByDepId(kategoriOri);

        if(!categoryDBOld.equals(categoryDBEdit)){
            count++;
        }

        // 3. deskripsi
        if(!inputAddProductModel.getDescription().equals(productDetailData.getInfo().getProductDescription())){
            count++;
        }

        // 4. compare preorder
        if(!productDetailData.getPreOrder().getPreorderProcessTime().equals(inputAddProductModel.getPreOrder()+"")){
            count++;
        }

        // 5. compare harga unit dan harga
        String currencyUnitEdit = inputAddProductModel.getCurrencyUnit().toLowerCase();
        String productPrice = productDetailData.getInfo().getProductPrice().replace(".","");
        String[] hargaDanUnitHarga = productPrice.split(" ");
        String UnitHargaOld = hargaDanUnitHarga[0].toLowerCase();
        if(!UnitHargaOld.equals(currencyUnitEdit)){
            count++;
        }

        String harga = hargaDanUnitHarga[1];
        if(!harga.equals(inputAddProductModel.getPrice())){
            count++;
        }


        // 6. compare berat unit dan berat
        String productWeightUnit = productDetailData.getInfo().getProductWeightUnit().toLowerCase();
        if(!inputAddProductModel.getWeightUnit().toLowerCase().contains(productWeightUnit)){
            count++;
        }

        String productWeightOld = productDetailData.getInfo().getProductWeight().replace(".", "");
        if(!inputAddProductModel.getWeight().equals(productWeightOld)){
            count++;
        }

        // 7. compare minimum order
        String productMinOrder = productDetailData.getInfo().getProductMinOrder().replace(".", "");
        if(Integer.parseInt(productMinOrder)!=inputAddProductModel.getMinimumOrder()){
            count++;
        }

        // 15. whole sales checking
        String wholeSaleChange = "";
//        List<WholeSaleAdapterModel> wholesalePrices = AddProductPresenterImpl.convertToWholeSaleAdapterModel(productDetailData.getWholesalePrice(),inputAddProductModel.getCurrencyUnit());
        List<ProductWholesalePrice> wholesalePrices = new ArrayList<>();
        for(EditProductForm.WholesalePrice wholesale : editProductForm.getWholesalePrice()){
            ProductWholesalePrice productWholesalePrice = new ProductWholesalePrice();
            productWholesalePrice.setWholesaleMax(wholesale.getWholesaleMax());
            productWholesalePrice.setWholesaleMin(wholesale.getWholesaleMin());
            productWholesalePrice.setWholesalePrice(wholesale.getWholesalePrice());
            wholesalePrices.add(productWholesalePrice);
        }
        List<WholeSaleAdapterModel> wholeSales = inputAddProductModel.getWholeSales();
        if(wholesalePrices.equals(wholeSales)){
            wholeSaleChange = "0";
        }else{
            wholeSaleChange = "1";
        }
//        if(wholesalePrices.size()==wholeSales.size()){
//            B : for(int i=0;i<wholesalePrices.size();i++){
//                ProductWholesalePrice oldData = wholesalePrices.get(i);
//                WholeSaleAdapterModel old = new WholeSaleAdapterModel(oldData.getWholesaleMin(), oldData.getWholesaleMax(), oldData.getWholesalePrice());
//                if(!old.equals(wholeSales.get(i))){
//                    count++;
//                    wholeSaleChange += "1";
//                    break B;
//                }
//                wholeSaleChange = "0";
//            }
//        }else{
//            wholeSaleChange += "1";
//        }

        // 16. catalog checking
        String catalogChange = "";
        long catalog = inputAddProductModel.getCatalog();
        long catalogOld = Long.parseLong(productDetailData.getInfo().getProductCatalogId());
        if(catalogOld!=catalog&&catalog!=-1){
            count++;
            catalogChange += "1";
        }else{
            catalogChange += "0";
        }

        // 8. compare etalase
        int productStatusOld = Integer.parseInt(productDetailData.getInfo().getProductStatus());
        long etalaseIdOld = Long.parseLong(productDetailData.getInfo().getProductEtalaseId());
        String PRD_STATE = "";
        switch(productStatusOld){
            case NetworkInteractor.PRD_STATE_ACTIVE:
                PRD_STATE = AddProductView.ETALASE_ETALASE;
                break;
            case NetworkInteractor.PRD_STATE_WAREHOUSE:
                PRD_STATE = AddProductView.ETALASE_GUDANG;
                break;
        }
        long etalaseId = inputAddProductModel.getEtalaseId();
        if(etalaseIdOld!=etalaseId || PRD_STATE.equals(inputAddProductModel.getStockStatus())){
            count++;
        }

        // set returnable 12
        Integer productReturnable = productDetailData.getInfo().getProductReturnable();
        if(productReturnable != inputAddProductModel.getReturnable() && inputAddProductModel.getReturnable() >= 0){
            count++;
        }

        // set bekas/baru 13
        String prodCondOld = productDetailData.getInfo().getProductCondition().toLowerCase();
        int prodCondNew = inputAddProductModel.getCondition();
        int condition = -1;
        switch (prodCondOld){
            case "bekas":
                condition = AddProductView.CONDITION_OLD;
                break;
            case "baru":
                condition = AddProductView.CONDITION_NEW;
                break;
        }

        if(prodCondNew!=condition){
            count++;
        }

        // set asuransi 14
        int asuranceNew = inputAddProductModel.getMustAsurance();
        int asuranceOld = -1;
        switch (productDetailData.getInfo().getProductInsurance().toLowerCase()){
            case "ya":
                asuranceOld = AddProductView.MUST_ASURANCE_YES;
                break;
            case "opsional":
                asuranceOld = AddProductView.MUST_ASURANCE_OPTIONAL;
                break;
        }
        if(asuranceNew!=asuranceOld){
            count++;
        }

        Pair<Integer, Map<String, String>> result = new Pair<>();
        result.setModel1(count);
        Map<String, String> hasil = new HashMap<>();
        hasil.put(ProductService.PRODUCT_CHANGE_CATALOG, catalogChange);
        hasil.put(ProductService.PRODUCT_CHANGE_WHOLESALE, wholeSaleChange);
        result.setModel2(hasil);
        return result;
    }

    @Parcel
    public static class ProductEditImage{
        public ImageModel oriImageModel;
        public int imageStatus = IMAGE_STILL;
        public ImageModel updateImageModel;
        public int position = -1;
    }
}
