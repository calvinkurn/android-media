package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase.IMAGE;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewValidateUseCase extends UseCase<SendReviewValidateDomain> {

    public static final String PARAM_REPUTATION_ID = "reputation_id";
    public static final String PARAM_REVIEW_ID = "review_id";
    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_REVIEW_MESSAGE = "review_message";
    public static final String PARAM_RATING = "rate_quality";
    public static final String PARAM_ANONYMOUS = "anonymous";
    public static final int DEFAULT_IS_ANONYMOUS = 1;

    private static final String PARAM_HAS_PRODUCT_REVIEW_PHOTO = "has_product_review_photo";
    private static final String PARAM_REVIEW_PHOTO_ALL = "product_review_photo_all";
    private static final String PARAM_REVIEW_PHOTO_OBJ = "product_review_photo_obj";


    private static final String PARAM_ATTACHMENT_ID = "attachment_id";
    private static final String PARAM_FILE_DESC = "file_desc";
    private static final String PARAM_IS_DELETED = "is_deleted";

    public static final int DEFAULT_NO_IMAGE = 0;
    public static final int DEFAULT_HAS_IMAGE = 1;

    protected ReputationRepository reputationRepository;

    public SendReviewValidateUseCase(ThreadExecutor threadExecutor, PostExecutionThread
            postExecutionThread, ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SendReviewValidateDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.sendReviewValidation(requestParams);
    }

    public static RequestParams getParam(String reviewId,
                                         String productId,
                                         String reputationId,
                                         String shopId,
                                         String rating,
                                         String reviewMessage,
                                         boolean isAnonymous) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_ID, reviewId);
        params.putString(PARAM_PRODUCT_ID, productId);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_RATING, rating);
        params.putString(PARAM_REVIEW_MESSAGE, reviewMessage);
        params.putInt(PARAM_HAS_PRODUCT_REVIEW_PHOTO, DEFAULT_NO_IMAGE);
        if(isAnonymous)
            params.putInt(PARAM_ANONYMOUS, DEFAULT_IS_ANONYMOUS);

        return params;
    }

    public static RequestParams getParamWithImage(String reviewId,
                                                  String productId,
                                                  String reputationId,
                                                  String shopId,
                                                  String rating,
                                                  String reviewMessage,
                                                  ArrayList<ImageUpload> list,
                                                  List<ImageUpload> deletedList,
                                                  boolean isAnonymous) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_ID, reviewId);
        params.putString(PARAM_PRODUCT_ID, productId);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_RATING, rating);
        params.putString(PARAM_REVIEW_MESSAGE, reviewMessage);
        params.putInt(PARAM_HAS_PRODUCT_REVIEW_PHOTO, DEFAULT_HAS_IMAGE);
        params.putString(PARAM_REVIEW_PHOTO_ALL, getReviewPhotos(list, deletedList));
        params.putString(PARAM_REVIEW_PHOTO_OBJ, getReviewPhotosObj(list, deletedList));
        if(isAnonymous)
            params.putInt(PARAM_ANONYMOUS, DEFAULT_IS_ANONYMOUS);

        return params;
    }


    private static String getReviewPhotos(ArrayList<ImageUpload> imageUploads,
                                          List<ImageUpload> deletedImageUploads) {
        String allPhoto = "";
        for (int i = 0; i < imageUploads.size(); i++) {
            if (imageUploads.size() == 1 || i == imageUploads.size() - 1) {
                allPhoto += imageUploads.get(i).getImageId();
            } else {
                allPhoto += imageUploads.get(i).getImageId() + "~";
            }
        }

        if (imageUploads.size() != 0 && deletedImageUploads.size() != 0) {
            allPhoto += "~";
        }

        for (int i = 0; i < deletedImageUploads.size(); i++) {
            if (deletedImageUploads.size() == 1 || i == deletedImageUploads.size() - 1) {
                allPhoto += deletedImageUploads.get(i).getImageId();
            } else {
                allPhoto += deletedImageUploads.get(i).getImageId()  + "~";
            }
        }
        return allPhoto;
    }

    private static String getReviewPhotosObj(ArrayList<ImageUpload> imageUploads, List<ImageUpload> deletedImageUploads) {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : imageUploads) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_FILE_DESC, image.getDescription());
                photoObj.put(PARAM_IS_DELETED, "0");
                if (isNewImage(image.getImageId())) {
                    photoObj.put(PARAM_ATTACHMENT_ID, "0");
                } else {
                    photoObj.put(PARAM_ATTACHMENT_ID, image.getImageId());
                }
                reviewPhotos.put(image.getImageId(), photoObj);
            }
            for (ImageUpload image : deletedImageUploads) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_FILE_DESC, image.getDescription());
                photoObj.put(PARAM_IS_DELETED, "1");
                if (isNewImage(image.getImageId())) {
                    photoObj.put(PARAM_ATTACHMENT_ID, "0");
                } else {
                    photoObj.put(PARAM_ATTACHMENT_ID, image.getImageId());
                }
                reviewPhotos.put(image.getImageId(), photoObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    public static boolean isNewImage(String imageId) {
        return imageId.startsWith(IMAGE);
    }
}
