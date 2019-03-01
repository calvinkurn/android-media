package com.tokopedia.contactus.inboxticket2.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.contactus.inboxticket2.data.model.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem;
import com.tokopedia.contactus.inboxticket2.domain.CreatedBy;
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse;
import com.tokopedia.contactus.inboxticket2.domain.RatingResponse;
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse;
import com.tokopedia.contactus.inboxticket2.domain.TicketDetailResponse;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketDetailUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.InboxOptionUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase2;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostRatingUseCase;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.contactus.orderquery.data.ImageUpload;
import com.tokopedia.contactus.orderquery.data.ImageUploadResult;
import com.tokopedia.contactus.orderquery.data.UploadImageContactUsParam;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.ToasterNormal;
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
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK;


public class InboxDetailPresenterImpl
        implements InboxDetailContract.InboxDetailPresenter, CustomEditText.Listener {

    private InboxDetailContract.InboxDetailView mView;
    private Tickets mTicketDetail;
    private GetTicketDetailUseCase mUsecase;
    private String fileUploaded;
    private List<Integer> searchIndices;
    private PostMessageUseCase postMessageUseCase;
    private PostMessageUseCase2 postMessageUseCase2;
    private PostRatingUseCase postRatingUseCase;
    private String NO = "NO";
    private String error;
    private static final int MESSAGE_WRONG_DIMENSION = 0;
    private static final int MESSAGE_WRONG_FILE_SIZE = 1;
    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private String rateCommentID;
    private int next;
    private Utils utils;
    private CreatedBy userData;
    private ArrayList<String> reasonList;
    private InboxOptionUseCase inboxOptionUseCase;

    public InboxDetailPresenterImpl(GetTicketDetailUseCase useCase,
                                    PostMessageUseCase messageUseCase,
                                    PostMessageUseCase2 messageUseCase2,
                                    PostRatingUseCase ratingUseCase, InboxOptionUseCase inboxOptionUseCase) {
        mUsecase = useCase;
        postMessageUseCase = messageUseCase;
        postMessageUseCase2 = messageUseCase2;
        postRatingUseCase = ratingUseCase;
        this.inboxOptionUseCase = inboxOptionUseCase;
    }

    @Override
    public void attachView(InboxBaseContract.InboxBaseView view) {
        mView = (InboxDetailContract.InboxDetailView) view;
        reasonList = new ArrayList<>(Arrays.asList(mView.getActivity().getResources().getStringArray(R.array.bad_reason_array)));
        getTicketDetails();
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
     if (requestCode == REQUEST_SUBMIT_FEEDBACK && resultCode == RESULT_OK) {
            mView.showMessage("Terima kasih atas masukannyaTerima kasih atas masukannya");
            mView.showIssueClosed();
            getTicketDetails();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public BottomSheetDialogFragment getBottomFragment(int resID) {
        InboxBottomSheetFragment bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID);
        bottomFragment.setPresenter(this);
        return bottomFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mView.toggleSearch(View.VISIBLE);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            if (mView.isSearchMode()) {
                mView.toggleSearch(View.GONE);
                if (mTicketDetail.isShowRating()) {
                    mView.toggleTextToolbar(View.GONE);
                } else if (mTicketDetail.getStatus().equalsIgnoreCase(getUtils().CLOSED)
                        && !mTicketDetail.isShowRating()) {
                    mView.showIssueClosed();
                } else {
                    mView.toggleTextToolbar(View.VISIBLE);
                }
                mView.clearSearch();
                mView.exitSearchMode();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void reAttachView() {

    }

    @Override
    public void clickCloseSearch() {

    }

    @Override
    public void onSearchSubmitted(String text) {
        text = text.trim();
        if (text.length() > 0) {
            search(text);
        } else {
            if (searchIndices != null)
                searchIndices.clear();
        }
    }

    @Override
    public void onSearchTextChanged(String text) {
        text = text.trim();
        if (text.length() > 0) {
            search(text);
        } else {
            if (searchIndices != null)
                searchIndices.clear();
        }
    }

    private void getTicketDetails() {
        mView.showProgressBar();
        inboxOptionUseCase.createRequestParams(mView.getActivity().getIntent().getStringExtra(InboxDetailActivity.PARAM_TICKET_ID));
        // TO get and cache the usecase options
        inboxOptionUseCase.execute(new Subscriber<ChipGetInboxDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ChipGetInboxDetail chipGetInboxDetail) {
                if (chipGetInboxDetail != null && chipGetInboxDetail.getData()  != null && chipGetInboxDetail.getData().getTickets() != null) {
                    mTicketDetail = chipGetInboxDetail.getData().getTickets();
                    CommentsItem topItem = new CommentsItem();
                    topItem.setAttachment(mTicketDetail.getAttachment());
                    topItem.setMessage(mTicketDetail.getMessage());
                    topItem.setCreatedBy(mTicketDetail.getCreatedBy());
                    topItem.setCreateTime(mTicketDetail.getCreateTime());
                    List<CommentsItem> commentsItems = mTicketDetail.getComments();
                    if (commentsItems == null) {
                        commentsItems = new ArrayList<>();
                        mTicketDetail.setComments(commentsItems);
                        commentsItems.add(topItem);
                    } else {
                        commentsItems.add(0, topItem);
                    }
                    for (CommentsItem item : commentsItems) {
                        if (userData == null) {
                            if (item.getCreatedBy().getRole().equals("customer")) {
                                userData = item.getCreatedBy();
                            }
                        }
                        String createTime = getUtils().getDateTime(item.getCreateTime());
                        item.setCreateTime(createTime);
                        int i;
                        int count = 0;
                        for (i = 0; i < createTime.length(); i++) {
                            char c = createTime.charAt(i);
                            if (c == ' ') {
                                count++;
                                if (count == 2)
                                    break;
                            }
                        }
                        item.setShortTime(createTime.substring(0, i));
                    }
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

    private boolean isBitmapDimenValid(String fileLoc) {
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
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError1);
        } else if (!isBitmapDimenValid(image.getFileLoc())) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION);
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError2);
        } else {
            mView.addImage(image);
        }
    }

    private void showErrorMessage(int messageWrongParam) {
        if (messageWrongParam == MESSAGE_WRONG_FILE_SIZE) {
            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.error_msg_wrong_size), true);
        } else if (messageWrongParam == MESSAGE_WRONG_DIMENSION) {
            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.error_msg_wrong_height_width), true);
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
        if (isValid >= 1) {
            Observable<List<ImageUpload>> uploadedImage = uploadFile(mView.getActivity(), mView.getImageList());
            uploadedImage.flatMap(imageUploads -> {
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
            }).flatMap(typeRestResponseMap -> {
                Type token = new TypeToken<InboxDataResponse<CreateTicketResult>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                InboxDataResponse ticketListResponse = res1.getData();
                CreateTicketResult createTicket = (CreateTicketResult) ticketListResponse.getData();
                if (createTicket != null && createTicket.getIsSuccess() > 0) {
                    if (createTicket.getPostKey() != null && createTicket.getPostKey().length() > 0) {
                        postMessageUseCase2.setQueryMap(fileUploaded, createTicket.getPostKey());
                        return postMessageUseCase2.createObservable(RequestParams.create());
                    } else {
                        error = (String) ticketListResponse.getErrorMessage().get(0);
                        return Observable.just(null);
                    }
                } else {
                    error = (String) ticketListResponse.getErrorMessage().get(0);
                    return Observable.just(null);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Map<Type, RestResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.hideSendProgress();
                            mView.setSnackBarErrorMessage(error, true);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                            mView.hideSendProgress();
                            if (typeRestResponseMap != null && !typeRestResponseMap.isEmpty()) {
                                Type token = new TypeToken<InboxDataResponse<StepTwoResponse>>() {
                                }.getType();
                                RestResponse res1 = typeRestResponseMap.get(token);
                                InboxDataResponse responseStep2 = res1.getData();
                                StepTwoResponse stepTwoResponse = (StepTwoResponse) responseStep2.getData();
                                if (stepTwoResponse != null && stepTwoResponse.getIsSuccess() > 0) {
                                    addNewLocalComment();
                                } else {
                                    mView.setSnackBarErrorMessage((String) responseStep2.getErrorMessage().get(0), true);
                                }
                            } else {
                                mView.setSnackBarErrorMessage(error, true);
                            }

                        }
                    });

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
                    Type token = new TypeToken<InboxDataResponse<CreateTicketResult>>() {
                    }.getType();
                    RestResponse res1 = typeRestResponseMap.get(token);
                    InboxDataResponse ticketListResponse = res1.getData();
                    CreateTicketResult createTicket = (CreateTicketResult) ticketListResponse.getData();
                    if (createTicket != null && createTicket.getIsSuccess() > 0) {
                        addNewLocalComment();
                    } else {
                        mView.setSnackBarErrorMessage((String) ticketListResponse.getErrorMessage().get(0), true);
                    }
                }
            });
        } else if (isValid == -1) {
            mView.setSnackBarErrorMessage(mView.getActivity().getResources().getString(R.string.invalid_images), true);
        }

    }

    @Override
    public void clickRate(int id, String commentID) {
        rateCommentID = commentID;
        if (id == R.id.btn_yes) {
            String YES = "YES";
            postRatingUseCase.setQueryMap(rateCommentID, YES, 0, 0, "");
            mView.showProgressBar();
            sendRating();
        } else {
            if (mTicketDetail.isShowBadCSATReason()) {
                mView.showBottomFragment();
            } else {
                postRatingUseCase.setQueryMap(rateCommentID, NO, 0, 0, "");
                mView.showProgressBar();
                mView.toggleTextToolbar(View.VISIBLE);
                sendRating();
            }
        }

    }

    @Override
    public void setBadRating(int position) {
        if (position > 0 && position < 7) {
            postRatingUseCase.setQueryMap(rateCommentID, NO, 1, position, "");
            mView.showProgressBar();
            mView.toggleTextToolbar(View.VISIBLE);
            sendRating();
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickReason,
                    reasonList.get(position - 1));
        }
    }

    @Override
    public void sendCustomReason(String customReason) {
        mView.showSendProgress();
        postRatingUseCase.setQueryMap(rateCommentID, NO, 1, 7, customReason);
        sendRating();
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickReason,
                customReason);
    }

    @Override
    public int getNextResult() {
        if (searchIndices != null && searchIndices.size() > 0) {
            if (next >= -1 && next < searchIndices.size() - 1) {
                mView.setCurrentRes(++next + 1);
                return searchIndices.get(next);
            } else {
                mView.showMessage(mView.getActivity().getResources().getString(R.string.cu_no_more_results));
                return -1;
            }
        } else {
            mView.showMessage(mView.getActivity().getResources().getString(R.string.no_search_result));
            return -1;
        }
    }

    @Override
    public int getPreviousResult() {
        if (searchIndices != null && searchIndices.size() > 0) {
            if (next > -1 && next < searchIndices.size()) {
                mView.setCurrentRes(next + 1);
                return searchIndices.get(next--);
            } else {
                mView.showMessage(mView.getActivity().getResources().getString(R.string.cu_no_more_results));
                return -1;
            }
        } else {
            mView.showMessage(mView.getActivity().getResources().getString(R.string.no_search_result));
            return -1;
        }
    }

    private Observable<List<ImageUpload>> uploadFile(final Context context, List<ImageUpload> imageUploads) {
        return Observable
                .from(imageUploads)
                .flatMap(imageUpload -> {
                    String uploadUrl = getUtils().UPLOAD_URL;
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

                    return Observable.zip(Observable.just(imageUpload), upload, (imageUpload1, imageUploadResult) -> {
                        if (imageUploadResult.getData() != null) {
                            imageUpload1.setPicSrc(imageUploadResult.getData().getPicSrc());
                            imageUpload1.setPicObj(imageUploadResult.getData().getPicObj());
                        } else if (imageUploadResult.getMessageError() != null) {
                            throw new RuntimeException(imageUploadResult.getMessageError());
                        }
                        return imageUpload1;
                    });
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList();
    }

    private int isUploadImageValid() {
        List<ImageUpload> uploadImageList = mView.getImageList();
        if (mTicketDetail.isNeedAttachment() && (uploadImageList == null || uploadImageList.isEmpty())) {
            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.attachment_required), true);
            mView.hideSendProgress();
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventNotAttachImageRequired,
                    "");
            return -2;
        }
        int numOfImages = uploadImageList.size();
        if (numOfImages > 0) {
            int count = 0;
            for (int item = 0; item < numOfImages; item++) {
                ImageUpload image = uploadImageList.get(item);
                if (fileSizeValid(image.getFileLoc()) && isBitmapDimenValid(image.getFileLoc())) {
                    count++;
                }
            }
            if (numOfImages == count) {
                return numOfImages;
            } else {
                return -1;
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
                Type token = new TypeToken<InboxDataResponse<RatingResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                InboxDataResponse ticketListResponse = res1.getData();
                RatingResponse ratingResponse = (RatingResponse) ticketListResponse.getData();
                if (ratingResponse.getIsSuccess() > 0) {
                    mView.hideBottomFragment();
                    mView.showMessage(mView.getActivity().getString(R.string.thanks_input));
                    if (mTicketDetail.getStatus().equals(getUtils().OPEN) || mTicketDetail.getStatus().equals(getUtils().SOLVED)) {
                        mView.toggleTextToolbar(View.VISIBLE);
                    } else {
                        mView.showIssueClosed();
                        mView.updateClosedStatus(mTicketDetail.getSubject());
                    }
                } else {
                    mView.setSnackBarErrorMessage((String) ticketListResponse.getErrorMessage().get(0), true);
                    mView.toggleTextToolbar(View.GONE);
                }
            }
        });
    }

    private void search(String searchText) {
        mView.showProgressBar();
        if (searchIndices == null)
            searchIndices = new ArrayList<>();
        searchIndices.clear();
        Observable.from(mTicketDetail.getComments()).map(new Func1<CommentsItem, Integer>() {
            private int count = -1;

            @Override
            public Integer call(CommentsItem commentsItem) {
                count++;
                if (utils.containsIgnoreCase(commentsItem.getMessagePlaintext(), searchText)) {
                    return count;
                } else {
                    return -1;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onCompleted() {
                        next = 0;
                        mView.hideProgressBar();
                        mView.enterSearchMode(searchText, searchIndices.size());
                        if (searchIndices.size() > 0) {
                            ContactUsTracking.sendGTMInboxTicket("",
                                    InboxTicketTracking.Category.EventInboxTicket,
                                    InboxTicketTracking.Action.EventClickSearchDetails,
                                    InboxTicketTracking.Label.GetResult);
                        } else {
                            mView.setSnackBarErrorMessage(mView.getActivity().getString(R.string.no_search_result), false);
                            ContactUsTracking.sendGTMInboxTicket("",
                                    InboxTicketTracking.Category.EventInboxTicket,
                                    InboxTicketTracking.Action.EventClickSearchDetails,
                                    InboxTicketTracking.Label.NoResult);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer != -1) {
                            searchIndices.add(integer);
                        }
                    }
                });

    }

    @Override
    public Utils getUtils() {
        if (utils == null) {
            utils = new Utils(mView.getActivity());
        }
        return utils;
    }

    @Override
    public void showImagePreview(int position, List<AttachmentItem> imagesLoc) {
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                InboxTicketTracking.Label.ImageAttached);
        ArrayList<String> imagesURL = new ArrayList<>();
        for (AttachmentItem item : imagesLoc) {
            if (item.getUrl() != null && !item.getUrl().isEmpty())
                imagesURL.add(item.getUrl());
            else
                imagesURL.add(item.getThumbnail());
        }
        mView.showImagePreview(position, imagesURL);
    }

    private void addNewLocalComment() {
        CommentsItem newItem = new CommentsItem();
        newItem.setCreatedBy(userData);
        newItem.setMessagePlaintext(mView.getUserMessage());
        newItem.setCreateTime(getUtils().getDateTimeCurrent());
        List<ImageUpload> uploadImageList = mView.getImageList();
        List<AttachmentItem> attachmentItems = new ArrayList<>();
        if (uploadImageList != null && !uploadImageList.isEmpty()) {
            for (ImageUpload upload : uploadImageList) {
                AttachmentItem attachment = new AttachmentItem();
                attachment.setThumbnail(upload.getFileLoc());
                attachmentItems.add(attachment);
            }
            newItem.setAttachment(attachmentItems);
        }

        mTicketDetail.getComments().add(newItem);
        mTicketDetail.setNeedAttachment(false);
        mView.updateAddComment();
    }
}
