package com.tokopedia.contactus.inboxticket2.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.domain.RatingResponse;
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse;
import com.tokopedia.contactus.inboxticket2.domain.TicketDetailResponse;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketDetailUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase2;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostRatingUseCase;
import com.tokopedia.contactus.inboxticket2.view.adapter.BadReasonAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.BottomSheetFragment;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.ImageUploadResult;
import com.tokopedia.contactus.orderquery.data.UploadImageContactUsParam;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by pranaymohapatra on 02/07/18.
 */

public class InboxDetailPresenterImpl
        implements InboxDetailContract.InboxDetailPresenter, CustomEditText.Listener {

    private InboxDetailContract.InboxDetailView mView;
    private Tickets mTicketDetail;
    private GetTicketDetailUseCase mUsecase;
    private String fileUploaded;
    private PostMessageUseCase postMessageUseCase;
    private PostMessageUseCase2 postMessageUseCase2;
    private PostRatingUseCase postRatingUseCase;
    private BadReasonAdapter badReasonAdapter;
    private static final int MESSAGE_WRONG_DIMENSION = 0;
    private static final int MESSAGE_WRONG_FILE_SIZE = 1;
    private static final String TAG = InboxDetailContract.InboxDetailPresenter.class.getSimpleName();
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";
    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private String rateCommentID;

    public InboxDetailPresenterImpl(GetTicketDetailUseCase useCase,
                                    PostMessageUseCase messageUseCase,
                                    PostMessageUseCase2 messageUseCase2,
                                    PostRatingUseCase ratingUseCase) {
        mUsecase = useCase;
        postMessageUseCase = messageUseCase;
        postMessageUseCase2 = messageUseCase2;
        postRatingUseCase = ratingUseCase;
    }

    @Override
    public void attachView(InboxBaseContract.InboxBaseView view) {
        mView = (InboxDetailContract.InboxDetailView) view;
        getTicketDetails(mView.getActivity().getIntent().getStringExtra("TICKET_ID"));
    }

    @Override
    public void detachView() {

    }

    @Override
    public CustomEditText.Listener getSearchListener() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }

    @Override
    public BottomSheetDialogFragment getBottomFragment() {
        BottomSheetFragment bottomFragment = new BottomSheetFragment();
        bottomFragment.setAdapter(getBadRatingAdapter());
        return bottomFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mView.toggleSearch(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public void reAttachView() {

    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    private void getTicketDetails(String id) {
        mView.showProgressBar();
        mUsecase.setTicketId(id);
        mUsecase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("ONERROR", e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<TicketDetailResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                TicketDetailResponse ticketDetailResponse = (TicketDetailResponse) ticketListResponse.getData();
                if (ticketDetailResponse != null && ticketDetailResponse.getTickets() != null) {
                    mTicketDetail = ticketDetailResponse.getTickets();
                    mView.renderMessageList(mTicketDetail);
                    mView.hideProgressBar();
                }
            }
        });

    }

    private String getFileUploaded(List<ImageUpload> attachment) {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : attachment) {
                reviewPhotos.put(image.getImageId(), image.getPicObj());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    private boolean fileSizeValid(String fileLoc) {
        File file = new File(fileLoc);
        long size = file.length();
        return ((size / 1024) < 10240);

    }

    private boolean getBitmapDimens(String fileLoc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(fileLoc).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return !(imageHeight < 300 && imageWidth < 300);
    }

    @Override
    public void onImageSelect(ImageUpload image) {
        if (!fileSizeValid(image.getFileLoc())) {
            showErrorMessage(MESSAGE_WRONG_FILE_SIZE);
        } else if (!getBitmapDimens(image.getFileLoc())) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION);
        } else {
            mView.addimage(image);
        }
    }

    private void showErrorMessage(int messageWrongParam) {
        if (messageWrongParam == MESSAGE_WRONG_FILE_SIZE) {
            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.error_msg_wrong_size));
        } else if (messageWrongParam == MESSAGE_WRONG_DIMENSION) {
            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.error_msg_wrong_height_width));
        }
    }

    @Override
    public TextWatcher watcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 15) {
                    mView.setSubmitButtonEnabled(true);
                } else {
                    mView.setSubmitButtonEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public void sendMessage() {
        mView.showSendProgress();
        int isValid = isUploadImageValid();
        if (isValid == 1) {
            Observable<List<ImageUpload>> uploadedImage = uploadFile(mView.getActivity(), mView.getImageList());
            uploadedImage.flatMap(new Func1<List<ImageUpload>, Observable<Map<Type, RestResponse>>>() {
                @Override
                public Observable<Map<Type, RestResponse>> call(List<ImageUpload> imageUploads) {
                    StringBuilder attachmentString = new StringBuilder();
                    for (ImageUpload imageUpload : imageUploads) {
                        if (imageUpload != null) {
                            attachmentString.append("~").append(imageUpload.getImageId());
                        }
                    }
                    attachmentString = new StringBuilder(attachmentString.toString().replace("~~", "~"));
                    if (attachmentString.length() > 0)
                        attachmentString = new StringBuilder(attachmentString.substring(1));
                    postMessageUseCase.setQueryMap(mTicketDetail.getId(),
                            mView.getUserMessage(), 1, attachmentString.toString());
                    fileUploaded = getFileUploaded(imageUploads);
                    return postMessageUseCase.createObservable(RequestParams.create()).subscribeOn(Schedulers.io());
                }
            }).flatMap(new Func1<Map<Type, RestResponse>, Observable<Map<Type, RestResponse>>>() {
                @Override
                public Observable<Map<Type, RestResponse>> call(Map<Type, RestResponse> typeRestResponseMap) {
                    Type token = new TypeToken<DataResponse<CreateTicketResult>>() {
                    }.getType();
                    RestResponse res1 = typeRestResponseMap.get(token);
                    DataResponse ticketListResponse = res1.getData();
                    CreateTicketResult createTicket = (CreateTicketResult) ticketListResponse.getData();
                    if (createTicket != null && createTicket.getIsSuccess() > 0) {
                        if (createTicket.getPostKey() != null && createTicket.getPostKey().length() > 0) {
                            postMessageUseCase2.setQueryMap(fileUploaded, createTicket.getPostKey());
                            return postMessageUseCase2.createObservable(RequestParams.create());
                        } else {
                            mView.hideSendProgress();
                            getTicketDetails(mTicketDetail.getId());
                            return Observable.just(null);
                        }
                    } else {
                        mView.hideSendProgress();
                        mView.setSnackBarErrorMessage("Anda baru saja membalas tiket. Silakan coba beberapa saat lagi.");
                        return Observable.just(null);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Map<Type, RestResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                            mView.hideSendProgress();
                            if (typeRestResponseMap != null && !typeRestResponseMap.isEmpty()) {
                                Type token = new TypeToken<DataResponse<StepTwoResponse>>() {
                                }.getType();
                                RestResponse res1 = typeRestResponseMap.get(token);
                                DataResponse responseStep2 = res1.getData();
                                StepTwoResponse stepTwoResponse = (StepTwoResponse) responseStep2.getData();
                                if (stepTwoResponse != null && stepTwoResponse.getIsSuccess() > 0) {
                                    getTicketDetails(mTicketDetail.getId());
                                } else {
                                    mView.setSnackBarErrorMessage("Maaf terjadi kesalahan teknis, silakan dicoba lagi.");
                                }
                            }

                        }
                    });
//            uploadedImage.subscribe(new Subscriber<List<ImageUpload>>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.e(TAG, e.getLocalizedMessage());
//                    e.printStackTrace();
//                    mView.hideSendProgress();
//                }
//
//                @Override
//                public void onNext(List<ImageUpload> imageUploads) {
//                    StringBuilder attachmentString = new StringBuilder();
//                    for (ImageUpload imageUpload : imageUploads) {
//                        if (imageUpload != null) {
//                            attachmentString.append("~").append(imageUpload.getImageId());
//                        }
//                    }
//                    attachmentString = new StringBuilder(attachmentString.toString().replace("~~", "~"));
//                    if (attachmentString.length() > 0)
//                        attachmentString = new StringBuilder(attachmentString.substring(1));
//                    postMessageUseCase.setQueryMap(mTicketDetail.getId(),
//                            mView.getUserMessage(), 1, attachmentString.toString());
//                    fileUploaded = getFileUploaded(imageUploads);
//                }
//            });

        } else if (isValid == 0) {
            postMessageUseCase.setQueryMap(mTicketDetail.getId(), mView.getUserMessage(), 0, "");
            postMessageUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.hideSendProgress();
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    mView.hideSendProgress();
                    Type token = new TypeToken<DataResponse<CreateTicketResult>>() {
                    }.getType();
                    RestResponse res1 = typeRestResponseMap.get(token);
                    DataResponse ticketListResponse = res1.getData();
                    CreateTicketResult createTicket = (CreateTicketResult) ticketListResponse.getData();
                    if (createTicket != null && createTicket.getIsSuccess() > 0) {
                        if (createTicket.getPostKey() != null && createTicket.getPostKey().length() > 0) {
                            postMessageUseCase2.setQueryMap(fileUploaded, createTicket.getPostKey());
                        } else {
                            mView.hideSendProgress();
                            getTicketDetails(mTicketDetail.getId());
                        }
                    } else {
                        mView.hideSendProgress();
                        mView.setSnackBarErrorMessage("Anda baru saja membalas tiket. Silakan coba beberapa saat lagi.");
                    }
                }
            });
        } else if (isValid == -1) {
            Log.d(TAG, "IMAGES NOT VALID");
            mView.setSnackBarErrorMessage("Images Not Valid");
        }

    }

    @Override
    public void clickRate(int id, String commentID) {
        rateCommentID = commentID;
        if (id == R.id.btn_yes) {
            postRatingUseCase.setQueryMap(rateCommentID, "YES", 0, 0, "");
            mView.showProgressBar();
            sendRating();
        } else {
            if (mTicketDetail.isShowBadCSATReason()) {
                mView.showBottomFragment();
            } else {
                postRatingUseCase.setQueryMap(rateCommentID, "NO", 0, 0, "");
                mView.showProgressBar();
                mView.toggleTextToolbar(View.VISIBLE);
                sendRating();
            }
        }

    }

    @Override
    public void setBadRating(int position) {
        mView.hideBottomFragment();
        if (position + 1 > 0 && position + 1 < 7) {
            postRatingUseCase.setQueryMap(rateCommentID, "NO", 1, position + 1, "");
            mView.showProgressBar();
            mView.toggleTextToolbar(View.VISIBLE);
            sendRating();
        } else {
            mView.askCustomReason();
        }


    }

    @Override
    public void sendCustomReason(String customReason) {
        mView.showSendProgress();
        postRatingUseCase.setQueryMap(rateCommentID, "NO", 1, 7, customReason);
        sendRating();
    }

    private Observable<List<ImageUpload>> uploadFile(final Context context, List<ImageUpload> imageUploads) {
        return Observable
                .from(imageUploads)
                .flatMap(new Func1<ImageUpload, Observable<ImageUpload>>() {
                    @Override
                    public Observable<ImageUpload> call(ImageUpload imageUpload) {
                        String uploadUrl = "http://u12.tokopedia.net";
                        NetworkCalculator networkCalculator = new NetworkCalculator(
                                NetworkConfig.POST, context,
                                uploadUrl)
                                .setIdentity()
                                .addParam(PARAM_IMAGE_ID, imageUpload.getImageId())
                                .addParam(PARAM_WEB_SERVICE, "1")
                                .compileAllParam()
                                .finish();

                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.getFileLoc()));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                                file);
                        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_IMAGE_ID));
                        RequestBody web_service = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_WEB_SERVICE));

                        Observable<ImageUploadResult> upload;
                        if (SessionHandler.isV4Login(context)) {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
                                    .uploadImage(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId, web_service

                                    );
                        } else {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
                                    .uploadImagePublic(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId,
                                            web_service);
                        }

                        return Observable.zip(Observable.just(imageUpload), upload, new Func2<ImageUpload, ImageUploadResult, ImageUpload>() {
                            @Override
                            public ImageUpload call(ImageUpload imageUpload, ImageUploadResult imageUploadResult) {
                                if (imageUploadResult.getData() != null) {
                                    imageUpload.setPicSrc(imageUploadResult.getData().getPicSrc());
                                    imageUpload.setPicObj(imageUploadResult.getData().getPicObj());
                                } else if (imageUploadResult.getMessageError() != null) {
                                    throw new RuntimeException(imageUploadResult.getMessageError());
                                }
                                return imageUpload;
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList();
    }

    private int isUploadImageValid() {
        List<ImageUpload> uploadImageList = mView.getImageList();
        int numOfImages = uploadImageList.size();
        if (numOfImages > 0) {
            for (int item = 0; item < numOfImages; item++) {
                ImageUpload image = uploadImageList.get(item);
                if (fileSizeValid(image.getFileLoc()) && getBitmapDimens(image.getFileLoc())) {
                    return 1;
                }
            }
        } else if (numOfImages == 0) {
            return 0;
        }
        return -1;
    }

    private void sendRating() {
        postRatingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                mView.hideProgressBar();
                mView.hideSendProgress();
                Type token = new TypeToken<DataResponse<RatingResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                RatingResponse ratingResponse = (RatingResponse) ticketListResponse.getData();
                if (ratingResponse.getIsSuccess() > 0) {
                    if (ratingResponse.getData().getRating().equals("YES")) {
                        mView.showIssueClosed();
                    } else {
                        mView.toggleTextToolbar(View.VISIBLE);
                        getTicketDetails(mTicketDetail.getId());
                    }
                }
            }
        });
    }

    private BadReasonAdapter getBadRatingAdapter() {
        if (badReasonAdapter == null) {
            List<String> badReasons = new ArrayList<>(Arrays.asList(mView.getActivity().getResources().getStringArray(R.array.bad_reason_array)));
            badReasonAdapter = new BadReasonAdapter(badReasons, mView.getActivity(), this);
        }
        return badReasonAdapter;
    }
}
