package com.tokopedia.core.myproduct.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core.R;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.myproduct.api.UploadImageProduct;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.model.ActResponseModelData;
import com.tokopedia.core.myproduct.model.AddProductPictureModel;
import com.tokopedia.core.myproduct.model.AddProductWithoutImageModel;
import com.tokopedia.core.myproduct.model.EditProductPictureModel;
import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.myproduct.model.InputAddProductModel;
import com.tokopedia.core.myproduct.model.ProductSubmitModel;
import com.tokopedia.core.myproduct.model.ProductValidationModel;
import com.tokopedia.core.myproduct.model.UploadProductImageData;
import com.tokopedia.core.myproduct.model.editproduct.EditProductModel;
import com.tokopedia.core.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.core.myproduct.presenter.AddProductView;
import com.tokopedia.core.myproduct.presenter.ManageProductPresenter;
import com.tokopedia.core.myproduct.utils.MessageErrorException;
import com.tokopedia.core.myproduct.utils.ProductEditHelper;
import com.tokopedia.core.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.network.apiservices.product.ProductActAfterService;
import com.tokopedia.core.network.apiservices.product.ProductActService;
import com.tokopedia.core.network.apiservices.product.apis.ProductActApi;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.TkpdUrl;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkErrorMessageEmpty;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * @author m.normansyah
 * @see com.tokopedia.core.myproduct.fragment.AddProductFragment
 * @since 29-12-2015
 * This class is built for class below
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ProductService extends IntentService implements ProductServiceConstant {

    public static final String HTTPS = "https://";
    public static final String WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_UPLOAD_PRODUCT_IMAGE_PL = "/web-service/v4/action/upload-image/upload_product_image.pl/";
    public static final String PIC_OBJ = "pic_obj";
    public static final String PRODUCT_CHANGE_CATALOG = "product_change_catalog";
    public static final String PRODUCT_CHANGE_WHOLESALE = "product_change_wholesale";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    private ResultReceiver receiver;
    private final Gson gson = new GsonBuilder().create();

    public static NotificationProductService notificationService;


    public ProductService() {
        super("ProductService");
    }

    public static void startDownload(Context context, DownloadResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ProductService.class);
        intent.putExtra(RECEIVER, receiver);

        // set mandatory param
        intent.putExtra(TkpdState.ProductService.SERVICE_TYPE, type);
        switch (type) {
            case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.ProductService.ADD_PRODUCT:
                Log.d(TAG, messageTAG + "try to upload product ");
                intent.putExtra(TkpdState.ProductService.PRODUCT_DB_ID, bundle.getLong(TkpdState.ProductService.PRODUCT_DB_ID));
                intent.putExtra(TkpdState.ProductService.PRODUCT_POSITION, bundle.getInt(TkpdState.ProductService.PRODUCT_POSITION));
                intent.putExtra(STOCK_STATUS, bundle.getString(STOCK_STATUS));
                break;
            case TkpdState.ProductService.EDIT_PRODUCT:
                Log.d(TAG, messageTAG + "try to edit product ");
                intent.putExtra(TkpdState.ProductService.PRODUCT_DB_ID, bundle.getLong(TkpdState.ProductService.PRODUCT_DB_ID));
                intent.putExtra(TkpdState.ProductService.PRODUCT_POSITION, bundle.getInt(TkpdState.ProductService.PRODUCT_POSITION));
                intent.putExtra(STOCK_STATUS, bundle.getString(STOCK_STATUS));
                ArrayList<ProductEditHelper.ProductEditImage> productEditImages
                        = Parcels.unwrap(bundle.getParcelable(PRODUCT_EDIT_PHOTOS));
                intent.putExtra(PRODUCT_EDIT_PHOTOS, Parcels.wrap(productEditImages));
                intent.putExtra(SHOP_ID, bundle.getString(SHOP_ID));
                intent.putExtra(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_CHANGE_WHOLESALE, bundle.getString(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_CHANGE_WHOLESALE));
                intent.putExtra(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_CHANGE_CATALOG, bundle.getString(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_CHANGE_CATALOG));
                intent.putExtra(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_NAME, bundle.getString(com.tokopedia.core.myproduct.service.ProductService.PRODUCT_NAME));
                break;
            case TkpdState.ProductService.DELETE_PRODUCT:
                Log.d(TAG, messageTAG + "try to delete product ");
                intent.putExtra(TkpdState.ProductService.PRODUCT_ID, bundle.getString(TkpdState.ProductService.PRODUCT_ID));
                break;
            default:
                throw new RuntimeException("unknown type for starting product service !!!");
        }

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, messageTAG + " Service Started!");

        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.INVALID_TYPE);

        int notificationId = intent.getIntExtra(NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getBaseContext(), TkpdUrl.GENERATE_HOST_V4)
                .setIdentity()
                .addParam(AddProductFragment.SERVER_LANGUAGE, AddProductFragment.GOLANG_VALUE)
                .compileAllParam()
                .finish();

        switch (type) {
            case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.ProductService.ADD_PRODUCT:
                sendProductServiceBroadcast(
                        type,
                        TkpdState.ProductService.STATUS_RUNNING,
                        TkpdState.ProductService.NO_PRODUCT_DB,
                        intent.getIntExtra(TkpdState.ProductService.PRODUCT_POSITION, 0),
                        "");

                // crawl from db product id
                long productId = intent.getLongExtra(TkpdState.ProductService.PRODUCT_DB_ID, -1);
                if (productId != (-1)) {
                    //[START] get produk from db
                    ProductDB produk = DbManagerImpl.getInstance().getProductDb(productId);
                    produk.setPictureDBs(produk.getImages());
                    produk.setWholesalePriceDBs(produk.getWholeSales());
                    //[END] get produk from db

                    InputAddProductModel inputAddProductModel = new InputAddProductModel();
                    inputAddProductModel.setProduk(produk);
                    inputAddProductModel.setNetworkCalculator(networkCalculator);
                    inputAddProductModel.setGeneratedHostActApi(RetrofitUtils.createRetrofit().create(GeneratedHostActApi.class));
                    inputAddProductModel.setPosition(intent.getIntExtra(TkpdState.ProductService.PRODUCT_POSITION, 0));
                    inputAddProductModel.setStockStatus(intent.getStringExtra(STOCK_STATUS));

                    Bundle retryBundle = intent.getExtras();
                    retryBundle.putLong(TkpdState.ProductService.PRODUCT_DB_ID, productId);
                    if (produk.getPictureDBs() == null || type == TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE) {
                        addProductEmptyImage(inputAddProductModel, retryBundle);
                    } else {
                        addProduct(inputAddProductModel, retryBundle);
                    }
                } else {
                    // notify to receiver that invalid data
                }
                break;
            case TkpdState.ProductService.EDIT_PRODUCT:
                sendRunningStatus(type, intent.getIntExtra(TkpdState.ProductService.PRODUCT_POSITION, 0));

                productId = intent.getLongExtra(TkpdState.ProductService.PRODUCT_DB_ID, -1);
                if (productId != (-1)) {
                    //[START] get produk from db
                    ProductDB produk =
                            DbManagerImpl.getInstance().getProductDb(productId);
                    produk.setPictureDBs(produk.getImages());
                    produk.setWholesalePriceDBs(produk.getWholeSales());
                    //[END] get produk from db

                    InputAddProductModel inputAddProductModel = new InputAddProductModel();
                    inputAddProductModel.setProduk(produk);
                    inputAddProductModel.setStockStatus(intent.getStringExtra(STOCK_STATUS));
                    ArrayList<ProductEditHelper.ProductEditImage> productEditImages
                            = Parcels.unwrap(intent.getParcelableExtra(PRODUCT_EDIT_PHOTOS));
                    inputAddProductModel.setProductEditImages(productEditImages);
                    inputAddProductModel.setShopId(intent.getStringExtra(SHOP_ID));
                    inputAddProductModel.setNetworkCalculator(networkCalculator);
                    inputAddProductModel.setProductChangeCatalog(intent.getStringExtra(PRODUCT_CHANGE_CATALOG));
                    inputAddProductModel.setProductChangeWholeSale(intent.getStringExtra(PRODUCT_CHANGE_WHOLESALE));
                    inputAddProductModel.setProductName(intent.getStringExtra(PRODUCT_NAME));
                    editProduct(inputAddProductModel);
                }
                break;
            case TkpdState.ProductService.DELETE_PRODUCT:
                String productIdDelete = intent.getStringExtra(TkpdState.ProductService.PRODUCT_ID);
                deleteProduct(productIdDelete + "");
                break;
        }
    }

    private void deleteProduct(String productId) {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", productId);
        new ProductActService().getApi().delete(AuthUtil.generateParams(getApplicationContext(), param))
                .map(new Func1<Response<TkpdResponse>, ActResponseModelData>() {
                    @Override
                    public ActResponseModelData call(Response<TkpdResponse> response) {
                        TkpdResponse body = response.body();
                        if (response.isSuccessful()) {
                            if (!checkErrorMessageEmpty(body.getErrorMessages().toString())) {
                                throw new RuntimeException(body.getErrorMessages().toString());
                            }
                        } else {
                            new RetrofitUtils.DefaultErrorHandler(response.code());
                        }

                        ActResponseModelData actResponseModel = body.convertDataObj(ActResponseModelData.class);

                        if (actResponseModel.getIsSuccess() != 1) {
                            throw new RuntimeException("Gagal menghapus produk");
                        }
                        return actResponseModel;
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new Subscriber<ActResponseModelData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e(TAG, messageTAG + e.getLocalizedMessage());

                            Bundle resultData = new Bundle();
                            resultData.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.DELETE_PRODUCT);
                            resultData.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, CommonUtils.generateMessageError(getApplicationContext(), e.getMessage()));
                            receiver.send(TkpdState.ProductService.STATUS_ERROR, resultData);
                        }
                    }

                    @Override
                    public void onNext(ActResponseModelData response) {
                        Bundle result = new Bundle();
                        result.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.DELETE_PRODUCT);
                        receiver.send(TkpdState.ProductService.STATUS_DONE, result);
                    }
                });
    }

    /**
     * just finished push to categorize image to push to ws.
     *
     * @param inputAddProductModel
     */
    private void editProduct(InputAddProductModel inputAddProductModel) {
        final int productId = inputAddProductModel.getProduk().getProductId();
        final String shopId = inputAddProductModel.getShopId();
        final String deviceId = inputAddProductModel.getNetworkCalculator().getDeviceId();
        final String userId = inputAddProductModel.getNetworkCalculator().getUserId();
        final String productChangeWholeSale = inputAddProductModel.getProductChangeWholeSale();
        final String productChangeCatalog = inputAddProductModel.getProductChangeCatalog();
//        notificationService = new NotificationProductService(this, inputAddProductModel.getProduk().getNameProd());
        Observable.just(inputAddProductModel)
                .flatMap(new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
                    @Override
                    public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                        Observable<List<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>>> list;
                        if (inputAddProductModel.getProductEditImages().isEmpty()) {
                            list = Observable.from(new ArrayList<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>>()).toList();
                        } else {
                            list =
                                    Observable
                                            .from(inputAddProductModel.getProductEditImages())
                                            .flatMap(
                                                    new Func1<ProductEditHelper.ProductEditImage, Observable<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>>>() {
                                                        @Override
                                                        public Observable<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>> call(ProductEditHelper.ProductEditImage productEditImage) {

                                                            Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>> result = new Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>();
                                                            Response<TkpdResponse> delete = null;
                                                            EditProductInputModel edit = null;
                                                            result.setModel1(productEditImage.position);// set position here

                                                            if (productEditImage.imageStatus == ProductEditHelper.IMAGE_CHANGED) {
                                                                Log.d(TAG, "supaya berubah jir muter2");

                                                                Pair<EditProductPictureModel, EditProductInputModel> inputModelPair = new Pair<EditProductPictureModel, EditProductInputModel>();
                                                                if (checkNotNull(productEditImage.oriImageModel.getPath())) {
                                                                    int pictureId = productEditImage.oriImageModel.getGambar().getPictureId();
                                                                    delete = deleteEditProduct(productId + "", userId, deviceId, pictureId + "", shopId).toBlocking().first();// delete here
                                                                    EditProductPictureModel editProductPic = getEditProductPictureModel(delete);// parsing here
                                                                    String is_success = editProductPic.getData().getIs_success();
                                                                    if (checkNotNull(is_success) && !is_success.isEmpty() && Integer.parseInt(is_success) == 1) {
                                                                        inputModelPair.setModel1(editProductPic);// save to the data
                                                                    } else {
                                                                        if (checkNotNull(editProductPic.getMessageError()) && !checkErrorMessageEmpty(editProductPic.getMessageError().toString())) {
                                                                            throw new MessageErrorException(editProductPic.getMessageError().toString());
                                                                        } else {
                                                                            new RetrofitUtils.DefaultErrorHandler(delete.code());
                                                                        }
                                                                    }

                                                                }

                                                                String path = productEditImage.updateImageModel.getPath();
                                                                try {
                                                                    edit = uploadEditProduct(ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(path)), productId + "", userId, deviceId).toBlocking().first();// upload picture
                                                                } catch (IOException e) {
                                                                    throw new MessageErrorException(getApplicationContext().getString(R.string.error_upload_image));
                                                                }
                                                                inputModelPair.setModel2(edit);// set result here

                                                                result.setModel2(inputModelPair);// save to the data
                                                            } else if (productEditImage.imageStatus == ProductEditHelper.IMAGE_REMOVED) {
                                                                int pictureId = productEditImage.updateImageModel.getGambar().getPictureId();
                                                                delete = deleteEditProduct(productId + "", userId, deviceId, pictureId + "", shopId).toBlocking().first();
                                                                EditProductPictureModel editProductPic = getEditProductPictureModel(delete);// parsing here
                                                                String is_success = editProductPic.getData().getIs_success();
                                                                Pair<EditProductPictureModel, EditProductInputModel> inputModelPair = new Pair<EditProductPictureModel, EditProductInputModel>();
                                                                if (checkNotNull(is_success) && !is_success.isEmpty() && Integer.parseInt(is_success) == 1) {
                                                                    inputModelPair.setModel1(editProductPic);// save to the data
                                                                    inputModelPair.setModel2(null);
                                                                } else {
                                                                    if (checkNotNull(editProductPic.getMessageError()) && !checkErrorMessageEmpty(editProductPic.getMessageError().toString())) {
                                                                        throw new MessageErrorException(editProductPic.getMessageError().toString());
                                                                    } else {
                                                                        new RetrofitUtils.DefaultErrorHandler(delete.code());
                                                                    }
                                                                }

                                                                result.setModel2(inputModelPair);
                                                            } else if (productEditImage.imageStatus == ProductEditHelper.IMAGE_ADDED) {
                                                                Pair<EditProductPictureModel, EditProductInputModel> inputModelPair = new Pair<EditProductPictureModel, EditProductInputModel>();

                                                                inputModelPair.setModel1(null);// there is no delete in here
                                                                String path = productEditImage.updateImageModel.getPath();
                                                                try {
                                                                    edit = uploadEditProduct(ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(path)), productId + "", userId, deviceId).toBlocking().first();// upload picture
                                                                } catch (IOException e) {
                                                                    throw new MessageErrorException(getApplicationContext().getString(R.string.error_upload_image));
                                                                }
                                                                inputModelPair.setModel2(edit);// set result here

                                                                result.setModel2(inputModelPair);// save to the data
                                                            }

                                                            return Observable.just(result);
                                                        }
                                                    }
                                            ).toList();
                        }

                        return Observable.zip(list, Observable.just(inputAddProductModel), new Func2<List<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>>, InputAddProductModel, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(List<Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>>> pairs, InputAddProductModel inputAddProductModel) {
                                inputAddProductModel.setNewPairs(pairs);
                                return inputAddProductModel;
                            }
                        });
                    }
                })
                .flatMap(new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
                    @Override
                    public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                        //[START] ambil picture id dari old product
                        List<String> picIds = new ArrayList<String>();
                        List<String> picDescs = new ArrayList<String>();

                        List<PictureDB> pictureDBs = inputAddProductModel.getProduk().getPictureDBs();
                        int primaryImagePosition = AddProductPresenterImpl.findPrimaryImage(pictureDBs);

                        Log.d(TAG, "[gambar] before product service " + pictureDBs.toString());
                        Log.e(TAG, "supaya berubah");

                        for (PictureDB pictureDB : pictureDBs) {
                            picIds.add(pictureDB.getPictureId() + "");
                            picDescs.add(pictureDB.getPictureDescription());
                        }
                        Log.d(TAG, messageTAG + " [gambar] before rearrange picture id " + picIds.toString());
                        //[START] ambil picture id dari old product

                        for (Pair<Integer, Pair<EditProductPictureModel, EditProductInputModel>> result :
                                inputAddProductModel.getNewPairs()) {
                            Integer position = result.getModel1();
                            EditProductPictureModel delete = result.getModel2().getModel1();
                            EditProductInputModel edit = result.getModel2().getModel2();

                            if (checkNotNull(edit)) {// this is changed picture
                                String picId = edit.editProductPictureModel.getData().getPicId();
                                picIds.set(position, picId);
                            } else if (checkNotNull(delete) && edit == null) {// this is for delete picture
                                picIds.remove(position);
                            }
                        }
                        String picIdsString = createRearrangePicIds(picIds);
                        Map<String, String> rearrangePicDescs = createRearrangePicDescs(picIds, picDescs);
                        String primaryPic = "";
                        if (primaryImagePosition != -1) {
                            primaryPic = picIds.get(primaryImagePosition);
                        }
                        Log.d(TAG, messageTAG + "[gambar] after rearrange picture id " + picIds.toString());

                        Observable<Response<TkpdResponse>> editProduct = editProduct(
                                userId + "",
                                deviceId + "",
                                productId + "",
                                inputAddProductModel.getProduk(),
                                inputAddProductModel.getStockStatus(),
                                primaryPic,
                                productChangeCatalog,
                                productChangeWholeSale,
                                picIdsString,
                                rearrangePicDescs,
                                inputAddProductModel.getProductName(),
                                (checkCollectionNotNull(inputAddProductModel.getNewPairs()))
                        );
                        return Observable.zip(editProduct, Observable.just(inputAddProductModel), new Func2<Response<TkpdResponse>, InputAddProductModel, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(Response<TkpdResponse> tkpdResponseResponse, InputAddProductModel inputAddProductModel) {
                                TkpdResponse body = tkpdResponseResponse.body();
                                if (tkpdResponseResponse.isSuccessful()) {
                                    if (!checkErrorMessageEmpty(body.getErrorMessages().toString())) {
                                        throw new MessageErrorException(body.getErrorMessages().toString());
                                    }
                                } else {
                                    new RetrofitUtils.DefaultErrorHandler(tkpdResponseResponse.code());
                                }

                                EditProductModel.Data editProductData = gson.fromJson(body.getStringData(), EditProductModel.Data.class);
                                inputAddProductModel.setEditProductData(editProductData);
                                return inputAddProductModel;
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InputAddProductModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e(TAG, messageTAG + e.getLocalizedMessage());

                            Bundle resultData = new Bundle();
                            resultData.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.EDIT_PRODUCT);
                            String error;
                            if(e instanceof MessageErrorException){
                                error = e.getMessage();
                            }else{
                                error = getString(R.string.error_connection_problem);
                            }
                            resultData.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, CommonUtils.generateMessageError(getApplicationContext(), error));
                            receiver.send(TkpdState.ProductService.STATUS_ERROR, resultData);
                        }
                    }

                    @Override
                    public void onNext(InputAddProductModel inputAddProductModel) {
                        if (inputAddProductModel.getEditProductData().getIsSuccess() == 1) {
                            Bundle result = new Bundle();
                            result.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.EDIT_PRODUCT);
                            result.putInt(TkpdState.ProductService.PRODUCT_ID, productId);
                            receiver.send(TkpdState.ProductService.STATUS_DONE, result);
                        }
                    }
                });
    }

    private static String createRearrangePicIds(List<String> Ids) {
        String test = "%s";
        String cacing = "~";
        String result = "";
        int count = 0;
        for (String id : Ids) {
            if (count == Ids.size() - 1) {
                result += String.format(test, id);
            } else {
                result += String.format(test, id) + cacing;
            }
            count++;
        }
        return result;
    }

    private static Map<String, String> createRearrangePicDescs(List<String> ids, List<String> productDescs) {
        Map<String, String> picDescReal = new HashMap<>();
        String test = "product_photo_desc%s";
        String result = "";
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            String productDesc = productDescs.get(i) != null ? productDescs.get(i) : "";
            String format = String.format(test, id);
            picDescReal.put(format, productDesc);
        }
        return picDescReal;
    }

    /**
     * parsing to real data
     *
     * @param delete
     * @return
     */
    @Nullable
    private EditProductPictureModel getEditProductPictureModel(Response<TkpdResponse> delete) {
        // parse here
        EditProductPictureModel editProductPic = null;
        if (checkNotNull(delete)) {
            if(delete.isSuccessful()) {
                TkpdResponse body = delete.body();
                editProductPic = gson.fromJson(body.getStrResponse(), EditProductPictureModel.class);
            }else{
                throw new RuntimeException(getString(R.string.error_connection_problem));
            }
        }else{
            throw new RuntimeException(getString(R.string.error_connection_problem));
        }
        return editProductPic;
    }

    private void addProduct(InputAddProductModel inputAddProductModel, final Bundle retryBundle) {
        notificationService = new NotificationProductService(this, 4 + inputAddProductModel.getProduk().getPictureDBs().size(),
                inputAddProductModel.getProduk().getNameProd());
        Observable.just(inputAddProductModel)
                .flatMap(
                        generateHost()
                )
                .doOnNext(new Action1<InputAddProductModel>() {
                    @Override
                    public void call(InputAddProductModel inputAddProductModel) {
                        if (notificationService != null) {
                            notificationService.updateNotificationProgress();
                        }
                    }
                })
                .flatMap(uploadImage())
                .doOnNext(new Action1<InputAddProductModel>() {
                    @Override
                    public void call(InputAddProductModel inputAddProductModel) {
                        if (notificationService != null) {
                            notificationService.updateNotificationProgress();
                        }
                    }
                })
                .flatMap(addProductValidation())
                .map(new Func1<InputAddProductModel, InputAddProductModel>() {
                    @Override
                    public InputAddProductModel call(InputAddProductModel inputAddProductModel) {
                        ProductValidationModel productValidationModel = inputAddProductModel.getProductValidationModel();
                        if (productValidationModel.getMessage_error() != null && productValidationModel.getMessage_error().length > 0) {
                            throw new MessageErrorException(Arrays.toString(productValidationModel.getMessage_error()));
                        }
                        return inputAddProductModel;
                    }
                })
                .doOnNext(new Action1<InputAddProductModel>() {
                    @Override
                    public void call(InputAddProductModel inputAddProductModel) {
                        if (notificationService != null) {
                            notificationService.updateNotificationProgress();
                        }
                    }
                })
                .flatMap(addProductPicture())
                .doOnNext(new Action1<InputAddProductModel>() {
                    @Override
                    public void call(InputAddProductModel inputAddProductModel) {
                        if (notificationService != null) {
                            notificationService.updateNotificationProgress();
                        }
                    }
                })
                .flatMap(addProductSubmit())
                .doOnNext(new Action1<InputAddProductModel>() {
                    @Override
                    public void call(InputAddProductModel inputAddProductModel) {
                        if (notificationService != null) {
                            notificationService.updateNotificationProgress();
                        }
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new Subscriber<InputAddProductModel>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, messageTAG + "onCompleted()!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e(TAG, messageTAG + e.getLocalizedMessage());

                            if(notificationService != null) {
                                String errorMessage = "";
                                if (e instanceof MessageErrorException){
                                    errorMessage = CommonUtils.generateMessageError(getApplicationContext(), e.getMessage());
                                }else{
                                    Crashlytics.logException(e);
                                    errorMessage = getString(R.string.msg_upload_error);
                                }

                                int requestCode = (int) ((System.currentTimeMillis() / 1000L)+new Random().nextInt(1000));
                                PendingIntent eIntent;

                                if (errorMessage.equals(getApplicationContext().getString(R.string.error_connection_problem)) ||
                                        errorMessage.equals(getApplicationContext().getString(R.string.error_bad_gateway))) {
                                    Intent errorIntent = new Intent(getApplicationContext(), ProductService.class);
                                    errorIntent.putExtras(retryBundle);
                                    errorIntent.putExtra(NOTIFICATION_ID, notificationService.getNotificationId());
                                    errorMessage += " - " + getApplicationContext().getString(R.string.retry_upload_product);
                                    eIntent = PendingIntent.getService(getApplicationContext(), requestCode, errorIntent, 0);
                                } else {
                                    Intent errorIntent = ProductActivity.moveToModifyProduct(getApplicationContext(), retryBundle.getLong(TkpdState.ProductService.PRODUCT_DB_ID));
                                    errorIntent.putExtra(NOTIFICATION_ID, notificationService.getNotificationId());
                                    errorMessage += " - " + getApplicationContext().getString(R.string.return_modify_product);
                                    eIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, errorIntent, 0);
                                }
                                notificationService.updateNotificationError(eIntent, errorMessage);
                            }

                            sendProductServiceBroadcast(
                                    TkpdState.ProductService.ADD_PRODUCT,
                                    TkpdState.ProductService.STATUS_ERROR,
                                    TkpdState.ProductService.NO_PRODUCT_DB,
                                    TkpdState.ProductService.NO_PRODUCT_POS,
                                    CommonUtils.generateMessageError(getApplicationContext(), e.getMessage())
                            );
                        }
                    }

                    @Override
                    public void onNext(InputAddProductModel inputAddProductModel) {
                        Log.d(TAG, messageTAG + " masuk sini !!! ");

                        if (notificationService != null) {
                            GlobalCacheManager cacheManager = new GlobalCacheManager();
                            cacheManager.delete(ManageProductPresenter.CACHE_KEY);
                            Intent pendingIntent = new Intent(getApplicationContext(), ManageProduct.class);
                            notificationService.updateNotificationCompleted(pendingIntent);
                            broadcastMessageComplete();
                        }

                        ProductDB produk = inputAddProductModel.getProduk();

                        //[START] update primary picture images
                        UploadProductImageData primaryPic = null;
                        for (Pair<PictureDB, UploadProductImageData> temp : inputAddProductModel.getUploadProductImageData()) {
                            if (temp.getModel1().getPicturePrimary() == PictureDB.PRIMARY_IMAGE) {
                                primaryPic = temp.getModel2();
                            }
                        }
                        if (primaryPic != null) {
                            PictureDB pictureDB = produk.getPictureDBs().get(0);
                            pictureDB.setPicturePrimary(1);
                            pictureDB.setPictureImageSourceUrl(primaryPic.getResult().getFilePath());
                            pictureDB.setPictureThumbnailUrl(primaryPic.getResult().getFileThumbnail());
                            pictureDB.save();
                        }
                        //[END] update primary picture images

                        //[START] update produk data
                        ProductSubmitModel productSubmitModel = inputAddProductModel.getProductSubmitModel();
                        if (productSubmitModel != null) {
                            produk.setProductId(Integer.parseInt(productSubmitModel.getData().getProductId()));
                            produk.setProductUrl(productSubmitModel.getData().getProductUrl());
                            produk.setSyncToServer(1);
                            produk.save();
                        }
                        //[END] update produk daata

                        //[START] hapus etalase tambah baru - kena FOREIGN KEY CONSTRAINT
                        List<EtalaseDB> etalase = DbManagerImpl.getInstance().removeEtalaseDb(-2);
                        Log.d(TAG, messageTAG+" hapus etalase-tambah-baru : "+etalase);
                        //[END] hapus etalase tambah baru - kena FOREIGN KEY CONSTRAINT

                        //[START] Send to UI if user images is one, if more than one send the rest
                        sendProductServiceBroadcast(
                                TkpdState.ProductService.ADD_PRODUCT,
                                TkpdState.ProductService.STATUS_DONE,
                                produk.getProductId(),
                                inputAddProductModel.getPosition(),
                                "");

                        //[END] Send to UI
                    }
                });
    }

    private void sendProductServiceBroadcast(int type, int status, long productId, int productPosition, String errorMessage) {
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.SERVICE_TYPE, type);
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, status);
        if(productId != -1) {
            bundle.putLong(TkpdState.ProductService.PRODUCT_DB_ID, productId);
        }
        if(productPosition != -1) {
            bundle.putInt(TkpdState.ProductService.PRODUCT_POSITION, productPosition);
        }
        if(!errorMessage.isEmpty()){
            bundle.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, errorMessage);
        }
        result.putExtras(bundle);
        sendBroadcast(result);
    }

    /**
     * tell other activity which implemment add product broadcast receiver that
     * the product has been successfully add to server
     */
    private void broadcastMessageComplete() {
        Intent intent = new Intent(ACTION_COMPLETED_ADD_PRODUCT);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @NonNull
    private Func1<InputAddProductModel, Observable<InputAddProductModel>> addProductSubmit() {
        return new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
            @Override
            public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                NetworkCalculator networkCalculator = inputAddProductModel.getNetworkCalculator();
                Map<String, String> param = new HashMap<>();
                param.put(CLICK_NAME, networkCalculator.getContent().get(CLICK_NAME));
                param.put(FILE_UPLOAD_TO, networkCalculator.getContent().get(FILE_UPLOAD_TO));
                param.put(POST_KEY, networkCalculator.getContent().get(POST_KEY));
                param.put(PRODUCT_ETALASE_ID, networkCalculator.getContent().get(PRODUCT_ETALASE_ID));
                param.put(PRODUCT_ETALASE_NAME, networkCalculator.getContent().get(PRODUCT_ETALASE_NAME));
                param.put(PRODUCT_UPLOAD_TO, networkCalculator.getContent().get(PRODUCT_UPLOAD_TO));

                Observable<ProductSubmitModel> submit = new ProductActService().getApi().addSubmit(AuthUtil.generateParams(getApplicationContext(), param));
                return Observable.zip(Observable.just(inputAddProductModel), submit,
                        new Func2<InputAddProductModel, ProductSubmitModel, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(InputAddProductModel inputAddProductModel, ProductSubmitModel productSubmitModel) {
                                inputAddProductModel.setProductSubmitModel(productSubmitModel);
                                return inputAddProductModel;
                            }
                        });
            }
        };
    }

    @NonNull
    private Func1<InputAddProductModel, Observable<InputAddProductModel>> addProductPicture() {
        return new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
            @Override
            public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                NetworkCalculator networkCalculator = inputAddProductModel.getNetworkCalculator();
                GenerateHostModel.GenerateHost generateHost = inputAddProductModel.getGenerateHostModel().getData().getGenerateHost();

                Map<String, String> param = new HashMap<>();
                param.put(DUPLICATE, networkCalculator.getContent().get(DUPLICATE));
                param.put(PRODUCT_PHOTO, networkCalculator.getContent().get(PRODUCT_PHOTO));
                param.put(PRODUCT_PHOTO_DEFAULT, networkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT));
                param.put(PRODUCT_PHOTO_DESCRIPTION, networkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION));
                param.put(SERVER_ID, networkCalculator.getContent().get(SERVER_ID));

                Log.d(TAG, "wkwkwk it happened");
                Observable<AddProductPictureModel> addProductPictureAfter = new ProductActAfterService(HTTPS + generateHost.getUploadHost() + "/web-service/v4/action/upload-image-helper/").getApi().addProductPictureAfter(AuthUtil.generateParams(getApplicationContext(), param));
//                Observable<AddProductPictureModel> addProductPictureAfter = RetrofitUtils.createRetrofit(HTTPS + generateHost.getUploadHost() + "/web-service/v4/action/upload-image-helper/add_product_picture.pl")
//                        .create(UploadImageProduct.class).addProductPictureAfter(
//                                HTTPS + generateHost.getUploadHost() + "/web-service/v4/action/upload-image-helper/add_product_picture.pl",
//                                NetworkCalculator.getContentMd5(networkCalculator),// 1
//                                NetworkCalculator.getDate(networkCalculator),// 2
//                                NetworkCalculator.getAuthorization(networkCalculator),// 3
//                                NetworkCalculator.getxMethod(networkCalculator),// 4
//                                NetworkCalculator.getUserId(getApplicationContext()),
//                                NetworkCalculator.getDeviceId(getApplicationContext()),
//                                NetworkCalculator.getHash(networkCalculator),
//                                networkCalculator.getDeviceTime(networkCalculator),
//                                networkCalculator.getContent().get(DUPLICATE),
//                                networkCalculator.getContent().get(PRODUCT_PHOTO),
//                                networkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT),
//                                networkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION),
//                                networkCalculator.getContent().get(SERVER_ID)
//                        );
                Log.d(TAG, messageTAG + " addProductPictureAfter [" + addProductPictureAfter + "]");
                return Observable.zip(Observable.just(inputAddProductModel), addProductPictureAfter
                        , new Func2<InputAddProductModel, AddProductPictureModel, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(InputAddProductModel inputAddProductModel, AddProductPictureModel addProductPictureModel) {
                                ProductDB produk = inputAddProductModel.getProduk();
                                NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(), TkpdUrl.PRODUCT_SUBMIT)
                                        .setIdentity()
                                        .addParam(CLICK_NAME, "")
                                        .addParam(FILE_UPLOAD_TO, addProductPictureModel.getData().getFile_uploaded())
                                        .addParam(POST_KEY, inputAddProductModel.getProductValidationModel().getData().getPostKey())
                                        .addParam(PRODUCT_ETALASE_ID, produk.getEtalaseDB().getEtalaseId() + "")
                                        .addParam(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName())
                                        .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE + "")
                                        .compileAllParam()
                                        .finish();
                                inputAddProductModel.setNetworkCalculator(networkCalculator);
                                inputAddProductModel.setAddProductPictureModel(addProductPictureModel);
                                return inputAddProductModel;
                            }
                        });
            }
        };
    }

    @NonNull
    private Func1<InputAddProductModel, Observable<InputAddProductModel>> addProductValidation() {
        return new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
            @Override
            public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                ProductDB produk = inputAddProductModel.getProduk();
                GenerateHostModel.GenerateHost generateHost = inputAddProductModel.getGenerateHostModel().getData().getGenerateHost();
                NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(), TkpdUrl.PRODUCT_VALIDATION)
                        .setIdentity()
                        .addParam(CLICK_NAME, "")
                        .addParam(DUPLICATE, DUPLICATE_NOT_COPY_FROM_OTHER_PRODUCT + "");

                if (produk.getCatalogid() == -1)
                    networkCalculator.addParam(PRODUCT_CATALOG_ID, "");
                else
                    networkCalculator.addParam(PRODUCT_CATALOG_ID, produk.getCatalogid() + "");

                networkCalculator
                        .addParam(PRODUCT_CONDITION, produk.getConditionProd() + "")
                        .addParam(PRODUCT_DEPARTMENT_ID, produk.getCategoryDB().getDepartmentId() + "")
                        .addParam(PRODUCT_DESCRIPTION, produk.getDescProd());

                //[START] Tambahkan dengan stock kosong atau stock tersedia
                if (produk.getEtalaseDB().getEtalaseId() == -2) {
                    networkCalculator.addParam(PRODUCT_ETALASE_ID, "new")
                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
                } else {
                    networkCalculator.addParam(PRODUCT_ETALASE_ID, produk.getEtalaseDB().getEtalaseId() + "")
                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
                }

                if (inputAddProductModel.getStockStatus().equals(AddProductView.ETALASE_GUDANG)) {
                    networkCalculator.addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_GUDANG + "");
                } else if (inputAddProductModel.getStockStatus().equals(AddProductView.ETALASE_ETALASE)) {
                    networkCalculator.addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE + "");
                }
                //[END] Tambahkan dengan stock kosong atau stock tersedia

                //[START] Tambahkan ke gudang, tambah baru dan tambah ke etalase yang ada
//                                if(produk.getEtalase().getEtalaseId()==-1){
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, "")
//                                            .addParam(PRODUCT_ETALASE_NAME, "")
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_GUDANG+"");
//                                }else if(produk.getEtalase().getEtalaseId()==-2){
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, "new")
//                                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalase().getEtalaseName())
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE+"");
//                                }else{
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, produk.getEtalase().getEtalaseId() + "")
//                                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalase().getEtalaseName())
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE+"");
//                                }
                // [END] Tambahkan ke gudang, tambah baru dan tambah ke etalase yang ada

                networkCalculator.addParam(PRODUCT_MIN_ORDER, produk.getMinOrderProd() + "")
                        .addParam(PRODUCT_MUST_INSURANCE, produk.getAssuranceProd() + "")
                        .addParam(PRODUCT_NAME, produk.getNameProd());


                List<Pair<PictureDB, UploadProductImageData>> uploadProductImageData = inputAddProductModel.getUploadProductImageData();
                String product_photo = "";
                String product_photo_desc = "";
                String product_photo_default = "";
                int imagesize = uploadProductImageData.size(), count = 1;
                for (Pair<PictureDB, UploadProductImageData> upi : uploadProductImageData) {
                    String filePath = upi.getModel2().getResult().getFilePath();
                    String pictureDescription = upi.getModel1().getPictureDescription();
                    if (upi.getModel1().getPicturePrimary() == PictureDB.PRIMARY_IMAGE) {
                        product_photo_default += (count - 1) + "";
                        Log.i(TAG, "Photo default result is index : " + product_photo_default);
                    }

                    if (count < imagesize) {
                        product_photo += filePath + "~";
                        if (checkNotNull(pictureDescription))
                            product_photo_desc += pictureDescription + "~";
                    } else {
                        product_photo += filePath;
                        if (checkNotNull(pictureDescription))
                            product_photo_desc += pictureDescription;
                    }

                    count++;
                }

                networkCalculator.addParam(PRODUCT_PHOTO, product_photo)
                        .addParam(PRODUCT_PHOTO_DEFAULT, product_photo_default);
                networkCalculator.addParam(PRODUCT_PHOTO_DESCRIPTION, product_photo_desc);
                //[END] This is set primary picture description


                networkCalculator
                        .addParam(PRODUCT_PRICE, formatToDecimal(produk.getPriceProd()))
                        .addParam(PRODUCT_PRICE_CURRENCY, produk.getUnitCurrencyDB().getWsInput() + "")
                        .addParam(PRODUCT_RETURNABLE, produk.getReturnableProd() + "")
                        .addParam(PRODUCT_WEIGHT, produk.getWeightProd() + "")
                        .addParam(PRODUCT_WEIGHT_UNIT, produk.getWeightUnitDB().getWsInput() + "");

                for (int i = 1; i <= produk.getWholesalePriceDBs().size(); i++) {
                    int index = i - 1;
                    switch (i) {
                        case 1:
                            networkCalculator.addParam(PRODUCT_PRC_1, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                            networkCalculator.addParam(QTY_MAX_1, produk.getWholesalePriceDBs().get(index).getMax() + "");
                            networkCalculator.addParam(QTY_MIN_1, produk.getWholesalePriceDBs().get(index).getMin() + "");
                            break;
                        case 2:
                            networkCalculator.addParam(PRODUCT_PRC_2, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                            networkCalculator.addParam(QTY_MAX_2, produk.getWholesalePriceDBs().get(index).getMax() + "");
                            networkCalculator.addParam(QTY_MIN_2, produk.getWholesalePriceDBs().get(index).getMin() + "");
                            break;
                        case 3:
                            networkCalculator.addParam(PRODUCT_PRC_3, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                            networkCalculator.addParam(QTY_MAX_3, produk.getWholesalePriceDBs().get(index).getMax() + "");
                            networkCalculator.addParam(QTY_MIN_3, produk.getWholesalePriceDBs().get(index).getMin() + "");
                            break;
                        case 4:
                            networkCalculator.addParam(PRODUCT_PRC_4, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                            networkCalculator.addParam(QTY_MAX_4, produk.getWholesalePriceDBs().get(index).getMax() + "");
                            networkCalculator.addParam(QTY_MIN_4, produk.getWholesalePriceDBs().get(index).getMin() + "");
                            break;
                        case 5:
                            networkCalculator.addParam(PRODUCT_PRC_5, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                            networkCalculator.addParam(QTY_MAX_5, produk.getWholesalePriceDBs().get(index).getMax() + "");
                            networkCalculator.addParam(QTY_MIN_5, produk.getWholesalePriceDBs().get(index).getMin() + "");
                            break;
                    }
                }

                if (produk.getProductPreOrder() != PO_PROCESS_TYPE_EMPTY) {
                    networkCalculator.addParam(PO_PROCESS_TYPE, PO_PROCESS_TYPE_DAY + "")
                            .addParam(PO_PROCESS_VALUE, produk.getProductPreOrder() + "");
                } else {
                    networkCalculator.addParam(PO_PROCESS_TYPE, "")
                            .addParam(PO_PROCESS_VALUE, "");
                }

                networkCalculator.addParam(SERVER_ID, generateHost.getServerId())
                        .compileAllParam()
                        .finish();
                inputAddProductModel.setNetworkCalculator(networkCalculator);

                Map<String, String> params = new HashMap<>();
                params.put(CLICK_NAME, networkCalculator.getContent().get(CLICK_NAME));
                params.put(DUPLICATE, networkCalculator.getContent().get(DUPLICATE));
                params.put(PRODUCT_CATALOG_ID, networkCalculator.getContent().get(PRODUCT_CATALOG_ID));
                params.put(PRODUCT_CONDITION, networkCalculator.getContent().get(PRODUCT_CONDITION));
                params.put(PRODUCT_DEPARTMENT_ID, networkCalculator.getContent().get(PRODUCT_DEPARTMENT_ID));
                params.put(PRODUCT_DESCRIPTION, networkCalculator.getContent().get(PRODUCT_DESCRIPTION));
                params.put(PRODUCT_ETALASE_ID, networkCalculator.getContent().get(PRODUCT_ETALASE_ID));
                params.put(PRODUCT_ETALASE_NAME, networkCalculator.getContent().get(PRODUCT_ETALASE_NAME));
                params.put(PRODUCT_MIN_ORDER, networkCalculator.getContent().get(PRODUCT_MIN_ORDER));
                params.put(PRODUCT_MUST_INSURANCE, networkCalculator.getContent().get(PRODUCT_MUST_INSURANCE));

                params.put(PRODUCT_NAME, networkCalculator.getContent().get(PRODUCT_NAME));
                params.put(PRODUCT_PHOTO, networkCalculator.getContent().get(PRODUCT_PHOTO));
                params.put(PRODUCT_PHOTO_DEFAULT, networkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT));
                params.put(PRODUCT_PHOTO_DESCRIPTION, networkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION));
                params.put(PRODUCT_PRICE, networkCalculator.getContent().get(PRODUCT_PRICE));
                params.put(PRODUCT_PRICE_CURRENCY, networkCalculator.getContent().get(PRODUCT_PRICE_CURRENCY));
                params.put(PRODUCT_RETURNABLE, networkCalculator.getContent().get(PRODUCT_RETURNABLE));
                params.put(PRODUCT_UPLOAD_TO, networkCalculator.getContent().get(PRODUCT_UPLOAD_TO));
                params.put(PRODUCT_WEIGHT, networkCalculator.getContent().get(PRODUCT_WEIGHT));
                params.put(PRODUCT_WEIGHT_UNIT, networkCalculator.getContent().get(PRODUCT_WEIGHT_UNIT));

                params.put(PRODUCT_PRC_1, networkCalculator.getContent().get(PRODUCT_PRC_1));
                params.put(PRODUCT_PRC_2, networkCalculator.getContent().get(PRODUCT_PRC_2));
                params.put(PRODUCT_PRC_3, networkCalculator.getContent().get(PRODUCT_PRC_3));
                params.put(PRODUCT_PRC_4, networkCalculator.getContent().get(PRODUCT_PRC_4));
                params.put(PRODUCT_PRC_5, networkCalculator.getContent().get(PRODUCT_PRC_5));
                params.put(QTY_MAX_1, networkCalculator.getContent().get(QTY_MAX_1));
                params.put(QTY_MAX_2, networkCalculator.getContent().get(QTY_MAX_2));
                params.put(QTY_MAX_3, networkCalculator.getContent().get(QTY_MAX_3));
                params.put(QTY_MAX_4, networkCalculator.getContent().get(QTY_MAX_4));
                params.put(QTY_MAX_5, networkCalculator.getContent().get(QTY_MAX_5));

                params.put(QTY_MIN_1, networkCalculator.getContent().get(QTY_MIN_1));
                params.put(QTY_MIN_2, networkCalculator.getContent().get(QTY_MIN_2));
                params.put(QTY_MIN_3, networkCalculator.getContent().get(QTY_MIN_3));
                params.put(QTY_MIN_4, networkCalculator.getContent().get(QTY_MIN_4));
                params.put(QTY_MIN_5, networkCalculator.getContent().get(QTY_MIN_5));
                params.put(PO_PROCESS_TYPE, networkCalculator.getContent().get(PO_PROCESS_TYPE));
                params.put(PO_PROCESS_VALUE, networkCalculator.getContent().get(PO_PROCESS_VALUE));
                params.put(SERVER_ID, networkCalculator.getContent().get(SERVER_ID));

                Observable<ProductValidationModel> productValidationModelObservable = new ProductActService().getApi().addValidation(AuthUtil.generateParams(getApplicationContext(), params));


//                Observable<ProductValidationModel> productValidationModelObservable = RetrofitUtils.createRetrofit().create(ProductActApi.class).addValidation(
//                        NetworkCalculator.getContentMd5(networkCalculator),// 1
//                        NetworkCalculator.getDate(networkCalculator),// 2
//                        NetworkCalculator.getAuthorization(networkCalculator),// 3
//                        NetworkCalculator.getxMethod(networkCalculator),// 4
//                        NetworkCalculator.getUserId(getApplicationContext()),
//                        NetworkCalculator.getDeviceId(getApplicationContext()),
//                        NetworkCalculator.getHash(networkCalculator),
//                        networkCalculator.getDeviceTime(networkCalculator),
//                        networkCalculator.getContent().get(CLICK_NAME),
//                        networkCalculator.getContent().get(DUPLICATE),
//                        networkCalculator.getContent().get(PRODUCT_CATALOG_ID),
//                        networkCalculator.getContent().get(PRODUCT_CONDITION),
//                        networkCalculator.getContent().get(PRODUCT_DEPARTMENT_ID),
//                        networkCalculator.getContent().get(PRODUCT_DESCRIPTION),
//                        networkCalculator.getContent().get(PRODUCT_ETALASE_ID),
//                        networkCalculator.getContent().get(PRODUCT_ETALASE_NAME),
//                        networkCalculator.getContent().get(PRODUCT_MIN_ORDER),
//                        networkCalculator.getContent().get(PRODUCT_MUST_INSURANCE),
//
//                        networkCalculator.getContent().get(PRODUCT_NAME),
//                        networkCalculator.getContent().get(PRODUCT_PHOTO),
//                        networkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT),
//                        networkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION),
//                        networkCalculator.getContent().get(PRODUCT_PRICE),
//                        networkCalculator.getContent().get(PRODUCT_PRICE_CURRENCY),
//                        networkCalculator.getContent().get(PRODUCT_RETURNABLE),
//                        networkCalculator.getContent().get(PRODUCT_UPLOAD_TO),
//                        networkCalculator.getContent().get(PRODUCT_WEIGHT),
//                        networkCalculator.getContent().get(PRODUCT_WEIGHT_UNIT),
//
//                        networkCalculator.getContent().get(PRODUCT_PRC_1),
//                        networkCalculator.getContent().get(PRODUCT_PRC_2),
//                        networkCalculator.getContent().get(PRODUCT_PRC_3),
//                        networkCalculator.getContent().get(PRODUCT_PRC_4),
//                        networkCalculator.getContent().get(PRODUCT_PRC_5),
//                        networkCalculator.getContent().get(QTY_MAX_1),
//                        networkCalculator.getContent().get(QTY_MAX_2),
//                        networkCalculator.getContent().get(QTY_MAX_3),
//                        networkCalculator.getContent().get(QTY_MAX_4),
//                        networkCalculator.getContent().get(QTY_MAX_5),
//
//                        networkCalculator.getContent().get(QTY_MIN_1),
//                        networkCalculator.getContent().get(QTY_MIN_2),
//                        networkCalculator.getContent().get(QTY_MIN_3),
//                        networkCalculator.getContent().get(QTY_MIN_4),
//                        networkCalculator.getContent().get(QTY_MIN_5),
//                        networkCalculator.getContent().get(PO_PROCESS_TYPE),
//                        networkCalculator.getContent().get(PO_PROCESS_VALUE),
//                        networkCalculator.getContent().get(SERVER_ID)
//                );
                return Observable.zip(Observable.just(inputAddProductModel), productValidationModelObservable, new Func2<InputAddProductModel, ProductValidationModel, InputAddProductModel>() {
                    @Override
                    public InputAddProductModel call(InputAddProductModel inputAddProductModel, ProductValidationModel productValidationModel) {
                        NetworkCalculator oldNetworkCalculator = inputAddProductModel.getNetworkCalculator();
                        GenerateHostModel.GenerateHost generateHost = inputAddProductModel.getGenerateHostModel().getData().getGenerateHost();
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext()
                                , HTTPS + generateHost.getUploadHost() + "/web-service/v4/action/upload-image-helper/add_product_picture.pl")
                                .setIdentity()
                                .addParam(DUPLICATE, "")
                                .addParam(PRODUCT_PHOTO, oldNetworkCalculator.getContent().get(PRODUCT_PHOTO))
                                .addParam(PRODUCT_PHOTO_DEFAULT, oldNetworkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT))
                                .addParam(PRODUCT_PHOTO_DESCRIPTION, oldNetworkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION))
                                .addParam(SERVER_ID, oldNetworkCalculator.getContent().get(SERVER_ID))
                                .compileAllParam()
                                .finish();
                        inputAddProductModel.setNetworkCalculator(networkCalculator);
                        inputAddProductModel.setProductValidationModel(productValidationModel);
                        return inputAddProductModel;
                    }
                });
            }
        };
    }

    @NonNull
    private Func1<InputAddProductModel, Observable<InputAddProductModel>> uploadImage() {
        return new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
            @Override
            public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {
                final NetworkCalculator networkCalculator = inputAddProductModel.getNetworkCalculator();
                GenerateHostModel.GenerateHost generateHost = inputAddProductModel.getGenerateHostModel().getData().getGenerateHost();
                final String url = HTTPS + generateHost.getUploadHost() + WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_UPLOAD_PRODUCT_IMAGE_PL;

                Observable<List<Pair<PictureDB, UploadProductImageData>>> listUploadImage = Observable.from(inputAddProductModel.getProduk().getPictureDBs())
                        .flatMap(new Func1<PictureDB, Observable<Pair<PictureDB, UploadProductImageData>>>() {
                            @Override
                            public Observable<Pair<PictureDB, UploadProductImageData>> call(PictureDB gambar) {
                                //[START] Somehow file couldn't be load for server
                                File file = null;
                                try {
                                    file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(gambar.getPath()));
                                } catch (IOException e) {
                                    throw new RuntimeException(getApplicationContext().getString(R.string.error_upload_image));
                                }
                                //[END] Somehow file couldn't be load for server

                                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                                RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                                RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
                                RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                                RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
                                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.NAME));
                                RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.NEW_ADD));
                                RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.PRODUCT_ID));
                                RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.SERVER_ID));
                                RequestBody token = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.TOKEN));

                                UploadProductImageData uploadProductImageData = RetrofitUtils.createRetrofit(url)
                                        .create(UploadImageProduct.class)
                                        .uploadProductV4(
                                                url,
                                                networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                                networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                                networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                                networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                                userId,
                                                deviceId,
                                                hash,
                                                deviceTime,
                                                fileToUpload,
                                                name,
                                                newAdd,
                                                productId,
                                                token,
                                                serverId
                                        ).toBlocking().first();

                                Log.d(TAG, messageTAG + " url [" + url + "] result [" + uploadProductImageData + "]");
                                Pair<PictureDB, UploadProductImageData> pair = new Pair<PictureDB, UploadProductImageData>(gambar, uploadProductImageData);

                                return Observable.just(pair);
                            }
                        })
                        .doOnNext(new Action1<Pair<PictureDB, UploadProductImageData>>() {
                            @Override
                            public void call(Pair<PictureDB, UploadProductImageData> pair) {
                                if (notificationService != null) {
                                    notificationService.updateNotificationProgress();
                                }
                            }
                        }).toList();


//                                    return uploadProductImageDataObservable;
                return Observable.zip(Observable.just(inputAddProductModel), listUploadImage,
                        new Func2<InputAddProductModel, List<Pair<PictureDB, UploadProductImageData>>, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(InputAddProductModel inputAddProductModel, List<Pair<PictureDB, UploadProductImageData>> listUploadImage) {
                                inputAddProductModel.setUploadProductImageData(listUploadImage);
                                return inputAddProductModel;
                            }
                        });
            }
        };
    }

    @NonNull
    private Func1<InputAddProductModel, Observable<InputAddProductModel>> generateHost() {
        return new Func1<InputAddProductModel, Observable<InputAddProductModel>>() {
            @Override
            public Observable<InputAddProductModel> call(InputAddProductModel inputAddProductModel) {

                NetworkCalculator networkCalculator = inputAddProductModel.getNetworkCalculator();

                HashMap<String, String> params = new HashMap<>();
                params.put(AddProductFragment.SERVER_LANGUAGE, networkCalculator.getContent().get(AddProductFragment.SERVER_LANGUAGE));
                Observable<GenerateHostModel> result = new GenerateHostActService().getApi().generateHost2(AuthUtil.generateParams(getApplicationContext(), params));
//                Observable<GenerateHostModel> result = inputAddProductModel.getGeneratedHostActApi().generateHost(
//                        NetworkCalculator.getContentMd5(networkCalculator),// 1
//                        NetworkCalculator.getDate(networkCalculator),// 2
//                        NetworkCalculator.getAuthorization(networkCalculator),// 3
//                        NetworkCalculator.getxMethod(networkCalculator),// 4
//                        NetworkCalculator.getUserId(getApplicationContext()),
//                        NetworkCalculator.getDeviceId(getApplicationContext()),
//                        NetworkCalculator.getHash(networkCalculator),
//                        networkCalculator.getDeviceTime(networkCalculator),
//                        networkCalculator.getContent().get(AddProductFragment.SERVER_LANGUAGE)
//                );
                return Observable.zip(Observable.just(inputAddProductModel)
                        , result,
                        new Func2<InputAddProductModel, GenerateHostModel, InputAddProductModel>() {
                            @Override
                            public InputAddProductModel call(InputAddProductModel inputAddProductModel, GenerateHostModel generateHostModel) {
                                GenerateHostModel.GenerateHost generateHost = generateHostModel.getData().getGenerateHost();
                                NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                                        HTTPS + generateHost.getUploadHost() + WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_UPLOAD_PRODUCT_IMAGE_PL)
                                        .setIdentity()
                                        .addParam(UploadPhotoTask.NAME, UploadPhotoTask.NAME_DEFAULT_IMAGE)
                                        .addParam(UploadPhotoTask.NEW_ADD, UploadPhotoTask.GOLANG)
                                        .addParam(UploadPhotoTask.PRODUCT_ID, UploadPhotoTask.PRODUCT_ID_DEFAULT_VALUE)
                                        .addParam(UploadPhotoTask.SERVER_ID, generateHost.getServerId())
                                        .addParam(UploadPhotoTask.TOKEN, UploadPhotoTask.TOKEN_DEFAULT_VALUE)
                                        .compileAllParam()
                                        .finish();
                                inputAddProductModel.setNetworkCalculator(networkCalculator);// calculate network calculator
                                inputAddProductModel.setGenerateHostModel(generateHostModel);// save generate host
                                return inputAddProductModel;
                            }
                        });
            }
        };
    }

    private void addProductEmptyImage(final InputAddProductModel inputAddProductModel, final Bundle retryBundle) {
        final String userId = SessionHandler.getLoginID(getApplicationContext());
        final String deviceId = GCMHandler.getRegistrationId(getApplicationContext());
        notificationService = new NotificationProductService(this, 1 + inputAddProductModel.getProduk().getPictureDBs().size(),
                inputAddProductModel.getProduk().getNameProd());
        Observable.just(inputAddProductModel)
                .flatMap(new Func1<InputAddProductModel, Observable<AddProductWithoutImageModel>>() {
                    @Override
                    public Observable<AddProductWithoutImageModel> call(InputAddProductModel inputAddProductModel) {
                        return addProductEmptyImage(userId, deviceId, inputAddProductModel);
                    }
                })
                .map(new Func1<AddProductWithoutImageModel, AddProductWithoutImageModel>() {
                    @Override
                    public AddProductWithoutImageModel call(AddProductWithoutImageModel addProductWithoutImageModel) {
                        ProductDB produk = inputAddProductModel.getProduk();
                        AddProductWithoutImageModel.Data data = addProductWithoutImageModel.getData();
                        if (data != null) {
                            produk.setProductId(data.getProductId());
                            produk.setProductUrl(data.getProductUrl());
                            produk.setDescProd(data.getProductDesc());
                            produk.setSyncToServer(1);
                            produk.save();
                        }
                        inputAddProductModel.setProduk(produk);
                        return addProductWithoutImageModel;
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new Subscriber<AddProductWithoutImageModel>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, messageTAG + "onCompleted()!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e(TAG, messageTAG + e.getLocalizedMessage());
                            if(notificationService != null) {
                                String errorMessage = "";
                                if (e instanceof MessageErrorException){
                                    errorMessage = CommonUtils.generateMessageError(getApplicationContext(), e.getMessage());
                                }else{
                                    Crashlytics.logException(e);
                                    errorMessage = getString(R.string.msg_upload_error);
                                }

                                int requestCode = (int) ((System.currentTimeMillis() / 1000L) + new Random().nextInt(1000));
                                PendingIntent eIntent;
                                if (errorMessage.equals(getApplicationContext().getString(R.string.error_connection_problem)) ||
                                        errorMessage.equals(getApplicationContext().getString(R.string.error_bad_gateway))) {
                                    Intent errorIntent = new Intent(getApplicationContext(), ProductService.class);
                                    errorIntent.putExtras(retryBundle);
                                    errorIntent.putExtra(NOTIFICATION_ID, notificationService.getNotificationId());
                                    errorMessage += " - " + getApplicationContext().getString(R.string.retry_upload_product);
                                    eIntent = PendingIntent.getService(getApplicationContext(), requestCode, errorIntent, 0);
                                } else {
                                    Intent errorIntent = ProductActivity.moveToModifyProduct(getApplicationContext(), retryBundle.getLong(TkpdState.ProductService.PRODUCT_DB_ID));
                                    errorIntent.putExtra(NOTIFICATION_ID, notificationService.getNotificationId());
                                    errorMessage += " - " + getApplicationContext().getString(R.string.return_modify_product);
                                    eIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, errorIntent, 0);
                                }
                                notificationService.updateNotificationError(eIntent, errorMessage);
                            }
                            Bundle resultData = new Bundle();
                            resultData.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.ADD_PRODUCT);
                            resultData.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, CommonUtils.generateMessageError(getApplicationContext(), e.getMessage()));
                            receiver.send(TkpdState.ProductService.STATUS_ERROR, resultData);
                        }
                    }

                    @Override
                    public void onNext(AddProductWithoutImageModel addProductWithoutImageModel) {
                        Log.d(TAG, messageTAG + " masuk sini !!! ");
                        ProductDB produk = inputAddProductModel.getProduk();

                        if (notificationService != null) {
                            GlobalCacheManager cacheManager = new GlobalCacheManager();
                            cacheManager.delete(ManageProductPresenter.CACHE_KEY);
                            Intent pendingIntent = new Intent(getApplicationContext(), ManageProduct.class);
                            notificationService.updateNotificationCompleted(pendingIntent);
                            broadcastMessageComplete();
                        }

                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        cacheManager.delete(ManageProductPresenter.CACHE_KEY);

                        //[START] Send to UI if user images is one, if more than one send the rest
                        Bundle result = new Bundle();
                        result.putInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE);
                        result.putLong(TkpdState.ProductService.PRODUCT_DB_ID, produk.getProductId());
                        result.putInt(TkpdState.ProductService.PRODUCT_POSITION, inputAddProductModel.getPosition());
                        receiver.send(TkpdState.ProductService.STATUS_DONE, result);
                        //[END] Send to UI
                    }
                });
    }

    public static Observable<AddProductWithoutImageModel> addProductEmptyImage(String userId, String deviceId, InputAddProductModel inputAddProductModel) {
        ProductDB produk = inputAddProductModel.getProduk();
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, userId, deviceId, TkpdUrl.PRODUCT_VALIDATION)
                .setIdentity()
                .addParam(CLICK_NAME, "")
                .addParam(DUPLICATE, DUPLICATE_NOT_COPY_FROM_OTHER_PRODUCT + "");

        if (produk.getCatalogid() == -1)
            networkCalculator.addParam(PRODUCT_CATALOG_ID, "");
        else
            networkCalculator.addParam(PRODUCT_CATALOG_ID, produk.getCatalogid() + "");

        networkCalculator
                .addParam(PRODUCT_CONDITION, produk.getConditionProd() + "")
                .addParam(PRODUCT_DEPARTMENT_ID, produk.getCategoryDB().getDepartmentId() + "")
                .addParam(PRODUCT_DESCRIPTION, produk.getDescProd());

        //[START] Tambahkan dengan stock kosong atau stock tersedia
        if (produk.getEtalaseDB().getEtalaseId() == -2) {
            networkCalculator.addParam(PRODUCT_ETALASE_ID, "new")
                    .addParam(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
        } else {
            networkCalculator.addParam(PRODUCT_ETALASE_ID, produk.getEtalaseDB().getEtalaseId() + "")
                    .addParam(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
        }

        if (inputAddProductModel.getStockStatus().equals(AddProductView.ETALASE_GUDANG)) {
            networkCalculator.addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_GUDANG + "");
        } else if (inputAddProductModel.getStockStatus().equals(AddProductView.ETALASE_ETALASE)) {
            networkCalculator.addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE + "");
        }
        //[END] Tambahkan dengan stock kosong atau stock tersedia

        networkCalculator.addParam(PRODUCT_MIN_ORDER, produk.getMinOrderProd() + "")
                .addParam(PRODUCT_MUST_INSURANCE, produk.getAssuranceProd() + "")
                .addParam(PRODUCT_NAME, produk.getNameProd());

        networkCalculator
                .addParam(PRODUCT_PRICE, formatToDecimal(produk.getPriceProd()))
                .addParam(PRODUCT_PRICE_CURRENCY, produk.getUnitCurrencyDB().getWsInput() + "")
                .addParam(PRODUCT_RETURNABLE, produk.getReturnableProd() + "")
                .addParam(PRODUCT_WEIGHT, produk.getWeightProd() + "")
                .addParam(PRODUCT_WEIGHT_UNIT, produk.getWeightUnitDB().getWsInput() + "");

        for (int i = 1; i <= produk.getWholesalePriceDBs().size(); i++) {
            int index = i - 1;
            switch (i) {
                case 1:
                    networkCalculator.addParam(PRODUCT_PRC_1, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    networkCalculator.addParam(QTY_MAX_1, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    networkCalculator.addParam(QTY_MIN_1, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 2:
                    networkCalculator.addParam(PRODUCT_PRC_2, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    networkCalculator.addParam(QTY_MAX_2, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    networkCalculator.addParam(QTY_MIN_2, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 3:
                    networkCalculator.addParam(PRODUCT_PRC_3, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    networkCalculator.addParam(QTY_MAX_3, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    networkCalculator.addParam(QTY_MIN_3, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 4:
                    networkCalculator.addParam(PRODUCT_PRC_4, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    networkCalculator.addParam(QTY_MAX_4, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    networkCalculator.addParam(QTY_MIN_4, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 5:
                    networkCalculator.addParam(PRODUCT_PRC_5, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    networkCalculator.addParam(QTY_MAX_5, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    networkCalculator.addParam(QTY_MIN_5, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
            }
        }

        if (produk.getProductPreOrder() != PO_PROCESS_TYPE_EMPTY) {
            networkCalculator.addParam(PO_PROCESS_TYPE, PO_PROCESS_TYPE_DAY + "")
                    .addParam(PO_PROCESS_VALUE, produk.getProductPreOrder() + "");
        } else {
            networkCalculator.addParam(PO_PROCESS_TYPE, "")
                    .addParam(PO_PROCESS_VALUE, "");
        }

        networkCalculator
                .compileAllParam()
                .finish();
        inputAddProductModel.setNetworkCalculator(networkCalculator);

        Map<String, String> params = new HashMap<>();
        params.put(CLICK_NAME, networkCalculator.getContent().get(CLICK_NAME));
        params.put(DUPLICATE, networkCalculator.getContent().get(DUPLICATE));
        params.put(PRODUCT_CATALOG_ID, networkCalculator.getContent().get(PRODUCT_CATALOG_ID));
        params.put(PRODUCT_CONDITION, networkCalculator.getContent().get(PRODUCT_CONDITION));
        params.put(PRODUCT_DEPARTMENT_ID, networkCalculator.getContent().get(PRODUCT_DEPARTMENT_ID));
        params.put(PRODUCT_DESCRIPTION, networkCalculator.getContent().get(PRODUCT_DESCRIPTION));
        params.put(PRODUCT_ETALASE_ID, networkCalculator.getContent().get(PRODUCT_ETALASE_ID));
        params.put(PRODUCT_ETALASE_NAME, networkCalculator.getContent().get(PRODUCT_ETALASE_NAME));
        params.put(PRODUCT_MIN_ORDER, networkCalculator.getContent().get(PRODUCT_MIN_ORDER));
        params.put(PRODUCT_MUST_INSURANCE, networkCalculator.getContent().get(PRODUCT_MUST_INSURANCE));

        params.put(PRODUCT_NAME, networkCalculator.getContent().get(PRODUCT_NAME));
        params.put(PRODUCT_PHOTO, networkCalculator.getContent().get(PRODUCT_PHOTO));
        params.put(PRODUCT_PHOTO_DEFAULT, networkCalculator.getContent().get(PRODUCT_PHOTO_DEFAULT));
        params.put(PRODUCT_PHOTO_DESCRIPTION, networkCalculator.getContent().get(PRODUCT_PHOTO_DESCRIPTION));
        params.put(PRODUCT_PRICE, networkCalculator.getContent().get(PRODUCT_PRICE));
        params.put(PRODUCT_PRICE_CURRENCY, networkCalculator.getContent().get(PRODUCT_PRICE_CURRENCY));
        params.put(PRODUCT_RETURNABLE, networkCalculator.getContent().get(PRODUCT_RETURNABLE));
        params.put(PRODUCT_UPLOAD_TO, networkCalculator.getContent().get(PRODUCT_UPLOAD_TO));
        params.put(PRODUCT_WEIGHT, networkCalculator.getContent().get(PRODUCT_WEIGHT));
        params.put(PRODUCT_WEIGHT_UNIT, networkCalculator.getContent().get(PRODUCT_WEIGHT_UNIT));

        params.put(PRODUCT_PRC_1, networkCalculator.getContent().get(PRODUCT_PRC_1));
        params.put(PRODUCT_PRC_2, networkCalculator.getContent().get(PRODUCT_PRC_2));
        params.put(PRODUCT_PRC_3, networkCalculator.getContent().get(PRODUCT_PRC_3));
        params.put(PRODUCT_PRC_4, networkCalculator.getContent().get(PRODUCT_PRC_4));
        params.put(PRODUCT_PRC_5, networkCalculator.getContent().get(PRODUCT_PRC_5));
        params.put(QTY_MAX_1, networkCalculator.getContent().get(QTY_MAX_1));
        params.put(QTY_MAX_2, networkCalculator.getContent().get(QTY_MAX_2));
        params.put(QTY_MAX_3, networkCalculator.getContent().get(QTY_MAX_3));
        params.put(QTY_MAX_4, networkCalculator.getContent().get(QTY_MAX_4));
        params.put(QTY_MAX_5, networkCalculator.getContent().get(QTY_MAX_5));

        params.put(QTY_MIN_1, networkCalculator.getContent().get(QTY_MIN_1));
        params.put(QTY_MIN_2, networkCalculator.getContent().get(QTY_MIN_2));
        params.put(QTY_MIN_3, networkCalculator.getContent().get(QTY_MIN_3));
        params.put(QTY_MIN_4, networkCalculator.getContent().get(QTY_MIN_4));
        params.put(QTY_MIN_5, networkCalculator.getContent().get(QTY_MIN_5));
        params.put(PO_PROCESS_TYPE, networkCalculator.getContent().get(PO_PROCESS_TYPE));
        params.put(PO_PROCESS_VALUE, networkCalculator.getContent().get(PO_PROCESS_VALUE));
        params.put(SERVER_ID, networkCalculator.getContent().get(SERVER_ID));

        return new ProductActService().getApi().addValidationWithoutImage(AuthUtil.generateParams(userId, deviceId, params));

    }

    private void sendRunningStatus(int type, int fragmentPosition) {
        /* Update UI: Product Service is running */
        Bundle running = new Bundle();
        running.putInt(TkpdState.ProductService.SERVICE_TYPE, type);
        running.putInt(TkpdState.ProductService.PRODUCT_POSITION, fragmentPosition);
        receiver.send(TkpdState.ProductService.STATUS_RUNNING, running);
    }

    public static Observable<GenerateHostModel> generateHost(String userId, String deviceId) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, userId, deviceId, TkpdUrl.GENERATE_HOST_V4)
                .setIdentity()
                .addParam(AddProductFragment.SERVER_LANGUAGE, AddProductFragment.GOLANG_VALUE)
                .compileAllParam()
                .finish();
        return RetrofitUtils.createRetrofit().create(GeneratedHostActApi.class).generateHost(
                NetworkCalculator.getContentMd5(networkCalculator),// 1
                NetworkCalculator.getDate(networkCalculator),// 2
                NetworkCalculator.getAuthorization(networkCalculator),// 3
                NetworkCalculator.getxMethod(networkCalculator),// 4
                userId,
                deviceId,
                NetworkCalculator.getHash(networkCalculator),
                NetworkCalculator.getDeviceTime(networkCalculator),
                networkCalculator.getContent().get(AddProductFragment.SERVER_LANGUAGE)
        );
    }

    public static Observable<UploadProductImageData> uploadProductFile(String userId, String deviceId, GenerateHostModel generateHostModel, File productFile) {
        return uploadProductFile(userId, deviceId, generateHostModel, productFile, UploadPhotoTask.PRODUCT_ID_DEFAULT_VALUE);
    }

    public static Observable<UploadProductImageData> uploadProductFile(String userId_, String deviceId_, GenerateHostModel generateHostModel, File productFile, String productId_) {
        GenerateHostModel.GenerateHost generateHost = generateHostModel.getData().getGenerateHost();
        String uploadProductUrl = HTTPS + generateHost.getUploadHost() + WEB_SERVICE_V4_ACTION_UPLOAD_IMAGE_UPLOAD_PRODUCT_IMAGE_PL;
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, userId_, deviceId_,
                uploadProductUrl)
                .setIdentity()
                .addParam(UploadPhotoTask.NAME, UploadPhotoTask.NAME_DEFAULT_IMAGE)
                .addParam(UploadPhotoTask.NEW_ADD, UploadPhotoTask.GOLANG)
                .addParam(UploadPhotoTask.PRODUCT_ID, productId_)
                .addParam(UploadPhotoTask.SERVER_ID, generateHost.getServerId())
                .addParam(UploadPhotoTask.TOKEN, UploadPhotoTask.TOKEN_DEFAULT_VALUE)
                .compileAllParam()
                .finish();

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), productFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.NAME));
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.NEW_ADD));
        RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.PRODUCT_ID));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.SERVER_ID));
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoTask.TOKEN));

        return RetrofitUtils.createRetrofit(uploadProductUrl)
                .create(UploadImageProduct.class)
                .uploadProductV4(
                        uploadProductUrl,
                        networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                        networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                        networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                        networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                        userId,
                        deviceId,
                        hash,
                        deviceTime,
                        fileToUpload,
                        name,
                        newAdd,
                        productId,
                        token,
                        serverId
                );
    }

    public static Observable<Response<TkpdResponse>> editProduct(String userId, String deviceId,
                                                                 String produkId, ProductDB produk, String stockStatus, String primaryPhotoId,
                                                                 String productChangeCatalog, String productChangeWholeSale, String photoRearrange,
                                                                 Map<String, String> photoDesc, String productName, boolean isPhotoChange) {
        Map<String, String> param = new HashMap<>();

        if (produk.getCatalogid() == -1)
            param.put(PRODUCT_CATALOG_ID, "");
        else
            param.put(PRODUCT_CATALOG_ID, produk.getCatalogid() + "");

        param.put(PRODUCT_CHANGE_CATALOG, productChangeCatalog); // kalau "0" berarti tidak ada perubahan, sedangkan "1" ada perubahan
//        param.put("product_change_photo","");
        param.put(PRODUCT_CHANGE_WHOLESALE, productChangeWholeSale); // kalau "0" berarti tidak ada perubahan, sedangkan "1" ada perubahan

        param.put(PRODUCT_CONDITION, produk.getConditionProd() + "");
        param.put(PRODUCT_DEPARTMENT_ID, produk.getCategoryDB().getDepartmentId() + "");
        param.put(PRODUCT_DESCRIPTION, produk.getDescProd());

        //[START] Tambahkan dengan stock kosong atau stock tersedia
        if (produk.getEtalaseDB().getEtalaseId() == -2) {
            param.put(PRODUCT_ETALASE_ID, "new");
            param.put(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
        } else {
            param.put(PRODUCT_ETALASE_ID, produk.getEtalaseDB().getEtalaseId() + "");
            param.put(PRODUCT_ETALASE_NAME, produk.getEtalaseDB().getEtalaseName());
        }

        if (stockStatus.equals(AddProductView.ETALASE_GUDANG)) {
            param.put(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_GUDANG + "");
        } else if (stockStatus.equals(AddProductView.ETALASE_ETALASE)) {
            param.put(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE + "");
        }
        //[END] Tambahkan dengan stock kosong atau stock tersedia

        param.put("product_id", produkId);

        //[START] Tambahkan ke gudang, tambah baru dan tambah ke etalase yang ada
//                                if(produk.getEtalase().getEtalaseId()==-1){
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, "")
//                                            .addParam(PRODUCT_ETALASE_NAME, "")
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_GUDANG+"");
//                                }else if(produk.getEtalase().getEtalaseId()==-2){
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, "new")
//                                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalase().getEtalaseName())
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE+"");
//                                }else{
//                                    networkCalculator.addParam(PRODUCT_ETALASE_ID, produk.getEtalase().getEtalaseId() + "")
//                                            .addParam(PRODUCT_ETALASE_NAME, produk.getEtalase().getEtalaseName())
//                                            .addParam(PRODUCT_UPLOAD_TO, PRODUCT_UPLOAD_TO_ETALASE+"");
//                                }
        // [END] Tambahkan ke gudang, tambah baru dan tambah ke etalase yang ada

        param.put(PRODUCT_MIN_ORDER, produk.getMinOrderProd() + "");
        param.put(PRODUCT_MUST_INSURANCE, produk.getAssuranceProd() + "");
        param.put(PRODUCT_NAME, productName);
        param.put(PRODUCT_PHOTO, photoRearrange);
        param.put(PRODUCT_PHOTO_DEFAULT, primaryPhotoId);

        //[START] This is set primary picture description
        param.putAll(photoDesc);
        //[END] This is set primary picture description


        param.put(PRODUCT_PRICE, formatToDecimal(produk.getPriceProd()));
        param.put(PRODUCT_PRICE_CURRENCY, produk.getUnitCurrencyDB().getWsInput() + "");
        param.put(PRODUCT_RETURNABLE, produk.getReturnableProd() + "");
        param.put(PRODUCT_WEIGHT, produk.getWeightProd() + "");
        param.put(PRODUCT_WEIGHT_UNIT, produk.getWeightUnitDB().getWsInput() + "");

        for (int i = 1; i <= produk.getWholesalePriceDBs().size(); i++) {
            int index = i - 1;
            switch (i) {
                case 1:
                    param.put(PRODUCT_PRC_1, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    param.put(QTY_MAX_1, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    param.put(QTY_MIN_1, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 2:
                    param.put(PRODUCT_PRC_2, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    param.put(QTY_MAX_2, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    param.put(QTY_MIN_2, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 3:
                    param.put(PRODUCT_PRC_3, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    param.put(QTY_MAX_3, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    param.put(QTY_MIN_3, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 4:
                    param.put(PRODUCT_PRC_4, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    param.put(QTY_MAX_4, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    param.put(QTY_MIN_4, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
                case 5:
                    param.put(PRODUCT_PRC_5, formatToDecimal(produk.getWholesalePriceDBs().get(index).getPriceWholesale()));
                    param.put(QTY_MAX_5, produk.getWholesalePriceDBs().get(index).getMax() + "");
                    param.put(QTY_MIN_5, produk.getWholesalePriceDBs().get(index).getMin() + "");
                    break;
            }
        }

        if (produk.getProductPreOrder() != PO_PROCESS_TYPE_EMPTY) {
            param.put(PO_PROCESS_TYPE, PO_PROCESS_TYPE_DAY + "");
            param.put(PO_PROCESS_VALUE, produk.getProductPreOrder() + "");
        } else {
            param.put(PO_PROCESS_TYPE, "");
            param.put(PO_PROCESS_VALUE, "");
        }

        if (isPhotoChange) {
            param.put("product_change_photo", "1");
        }

        return new ProductActService().getApi().edit(AuthUtil.generateParams(userId, deviceId, param));
    }

    public static Observable<EditProductPictureModel> editProductPicture(String userId_, String deviceId_, UploadProductImageData uploadProductImageData) {
        String editProductPictureUrl = TkpdBaseURL.Product.URL_PRODUCT_ACTION + TkpdBaseURL.Product.PATH_EDIT_PICTURE;
        NetworkCalculator net = new NetworkCalculator(NetworkConfig.POST, userId_, deviceId_, editProductPictureUrl)
                .setIdentity()
                .addParam(PIC_OBJ, uploadProductImageData.getResult().getPicObj())
                .compileAllParam()
                .finish();
        return RetrofitUtils.createRetrofit().create(ProductActApi.class).editPicture(
                NetworkCalculator.getContentMd5(net),// 1
                NetworkCalculator.getDate(net),// 2
                NetworkCalculator.getAuthorization(net),// 3
                NetworkCalculator.getxMethod(net),// 4
                userId_,
                deviceId_,
                NetworkCalculator.getHash(net),
                NetworkCalculator.getDeviceTime(net),
                net.getContent().get(PIC_OBJ)
        );
    }

    public Observable<Response<TkpdResponse>> deleteEditProduct(String productId, String userId, String deviceId, String pictureId, String shopId) {
        Map<String, String> additionalParam = new HashMap<>();
        additionalParam.put("shop_id", shopId);
        additionalParam.put("product_id", productId);
        additionalParam.put("picture_id", pictureId);
        Observable<Response<TkpdResponse>> responseObservable = new ProductActService().getApi().deletePicture(
                AuthUtil.generateParams(userId, deviceId, additionalParam)
        );

        return responseObservable;
    }

    /***
     * call this to upload product file
     *
     * @param productFile
     * @param productId
     * @param userId
     * @param deviceId
     * @return
     */
    public Observable<EditProductInputModel> uploadEditProduct(File productFile, final String productId, final String userId, final String deviceId) {
        return Observable.just(new EditProductInputModel(productFile, productId, userId, deviceId))
                .flatMap(new Func1<EditProductInputModel, Observable<EditProductInputModel>>() {
                    @Override
                    public Observable<EditProductInputModel> call(EditProductInputModel editProductInputModel) {
                        Observable<GenerateHostModel> generateHost = generateHost(userId, deviceId);
                        return Observable.zip(Observable.just(editProductInputModel), generateHost, new Func2<EditProductInputModel, GenerateHostModel, EditProductInputModel>() {
                            @Override
                            public EditProductInputModel call(EditProductInputModel editProductInputModel, GenerateHostModel generateHostModel) {
                                editProductInputModel.generateHostModel = generateHostModel;
                                return editProductInputModel;
                            }
                        });
                    }
                })
                .flatMap(new Func1<EditProductInputModel, Observable<EditProductInputModel>>() {
                    @Override
                    public Observable<EditProductInputModel> call(EditProductInputModel editProductInputModel) {
                        Observable<UploadProductImageData> uploadFile = uploadProductFile(userId, deviceId,
                                editProductInputModel.generateHostModel, editProductInputModel.productFile, productId);

                        return Observable.zip(uploadFile, Observable.just(editProductInputModel), new Func2<UploadProductImageData, EditProductInputModel, EditProductInputModel>() {
                            @Override
                            public EditProductInputModel call(UploadProductImageData uploadProductImageData, EditProductInputModel editProductInputModel) {
                                editProductInputModel.uploadProductImageData = uploadProductImageData;
                                return editProductInputModel;
                            }
                        });
                    }
                }).flatMap(new Func1<EditProductInputModel, Observable<EditProductInputModel>>() {
                    @Override
                    public Observable<EditProductInputModel> call(EditProductInputModel editProductInputModel) {
                        Observable<EditProductPictureModel> editPic = editProductPicture(userId, deviceId, editProductInputModel.uploadProductImageData);
                        return Observable.zip(editPic, Observable.just(editProductInputModel), new Func2<EditProductPictureModel, EditProductInputModel, EditProductInputModel>() {
                            @Override
                            public EditProductInputModel call(EditProductPictureModel editProductPictureModel, EditProductInputModel editProductInputModel) {
                                editProductInputModel.editProductPictureModel = editProductPictureModel;
                                return editProductInputModel;
                            }
                        });
                    }
                });
    }

    public static class EditProductInputModel {
        public EditProductInputModel(File productFile, String productId, String userId, String deviceId) {
            this.deviceId = deviceId;
            this.userId = userId;
            this.productFile = productFile;
            this.productId = productId;
        }

        String userId;
        String deviceId;
        File productFile;
        String productId;
        EditProductPictureModel editProductPictureModel;
        UploadProductImageData uploadProductImageData;
        GenerateHostModel generateHostModel;
    }

    private static String formatToDecimal(Double input) {
        NumberFormat nf = new DecimalFormat("##.###");
        return nf.format(input);
    }


    public class NotificationProductService {
        public static final String BITMAP = "bitmap";
        public static final String TOKOPEDIA = "TOKOPEDIA";
        public static final String ON_PROGRESS_UPLOADING = "Sedang Mengunggah";
        public static final String UPLOADING = "Mengunggah ";
        public static final String COM_TOKOPEDIA = "com.tokopedia";
        public static final String BITMAP_PARAM = "Test";
        public static final String ON_COMPLETE_UPLOADING = "Berhasil Menambah Produk : ";
        public static final String ON_FAILED_UPLOADING = "Gagal Menambah Produk";

        private Context context;
        private NotificationManager notificationManager;
        private NotificationCompat.Builder notification;
        private int notificationId;
        private int notificationCounter;
        private int notificationProgress = 0;

        private String title;
        private String contextText;
        private String group;
        private String bitmapParam;

        public NotificationProductService(Context context, int progress, String productName) {
            this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            this.notification = new NotificationCompat.Builder(context);
            this.context = context;
            this.notificationProgress = 100 / progress;
            this.notificationCounter = 0;
            notificationId = (int) (Math.random() * 50 + 1);
            title = UPLOADING + productName;
            contextText = productName;
            group = COM_TOKOPEDIA;
            bitmapParam = BITMAP_PARAM;
            updateNotificationProgress();
        }

        public NotificationProductService(Context context, String productName) {
            this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            this.notification = new NotificationCompat.Builder(context);
            this.context = context;
            this.notificationCounter = 0;
            notificationId = (int) (Math.random() * 50 + 1);
            title = TOKOPEDIA;
            contextText = productName;
            group = COM_TOKOPEDIA;
            bitmapParam = BITMAP_PARAM;
            updateNotificationProgress();
        }

        private void updateNotificationProgress() {
            // Construct pending intent to serve as action for notification item
            Intent pendingIntent = new Intent(context, ManageProduct.class);
            pendingIntent.putExtra(BITMAP, bitmapParam);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, pendingIntent, 0);
            notification
                    .setContentTitle(title)
                    .setContentText(ON_PROGRESS_UPLOADING)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(ON_PROGRESS_UPLOADING))
                    .setContentIntent(pIntent)
                    .setSmallIcon(getDrawableIcon())
                    .setGroup(group);
            if (notificationProgress == 0) {
                notification.setProgress(0, 0, true);
            } else {
                notification.setProgress(100, notificationCounter * notificationProgress, false);
            }
            Notification builtNotification = notification.build();
            notificationManager.notify(notificationId, builtNotification);
            notificationCounter++;
        }

        private void updateNotificationCompleted(Intent pendingIntent) {
            notificationManager.cancel(notificationId);

            PendingIntent pIntent = PendingIntent.getActivity(context, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            notification.setContentTitle(title)
            notification.setContentTitle(ON_COMPLETE_UPLOADING + contextText)
                    .setContentIntent(pIntent)
                    .setContentText(ON_COMPLETE_UPLOADING + contextText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(ON_COMPLETE_UPLOADING + contextText))
                    .setAutoCancel(true)
                    .setSmallIcon(getDrawableIcon())
                    .setGroup(group)
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            setRingtone();

            // this is needed because the main notification used to show progress is ongoing
            // and a new one has to be created to allow the user to dismiss it
            notificationManager.notify(notificationId, notification.build());

        }

        private void updateNotificationError(PendingIntent errorIntent, String errorMessage) {
            notificationManager.cancel(notificationId);


            notification.setContentTitle(title)
                    .setContentText(errorMessage)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(errorMessage))
                    .setContentIntent(errorIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(getDrawableIcon())
                    .setGroup(group)
                    .setProgress(0, 0, false).setOngoing(false);
            setRingtone();

            notificationManager.notify(notificationId, notification.build());
        }

        private void setRingtone() {
            notification.setSound(RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION));
            notification.setOnlyAlertOnce(false);
        }

        public int getNotificationId() {
            return notificationId;
        }

    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.qc_launcher2;
        else
            return R.drawable.ic_stat_notify;
    }

}
