package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.*
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.contactus.orderquery.data.CreateTicketResult
import com.tokopedia.contactus.orderquery.data.ImageUpload
import com.tokopedia.contactus.orderquery.data.ImageUploadResult
import com.tokopedia.contactus.orderquery.data.UploadImageContactUsParam
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils
import com.tokopedia.core.network.v4.NetworkConfig
import com.tokopedia.core.util.ImageUploadHandler
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class InboxDetailPresenterImpl(private val postMessageUseCase: PostMessageUseCase,
                               private val postMessageUseCase2: PostMessageUseCase2,
                               private val postRatingUseCase: PostRatingUseCase,
                               private val inboxOptionUseCase: InboxOptionUseCase,
                               private val submitRatingUseCase: SubmitRatingUseCase,
                               private val closeTicketByUserUseCase: CloseTicketByUserUseCase) : InboxDetailPresenter, CustomEditText.Listener, CoroutineScope {
    private var mView: InboxDetailView? = null
    private var mTicketDetail: Tickets? = null
    private var fileUploaded: String? = null
    private var searchIndices: MutableList<Int>? = null
    private val NO = "NO"
    private var error: String? = null
    private val rateCommentID: String? = null
    private var next = 0
    private var utils: Utils? = null
    private var userData: CreatedBy? = null
    private var reasonList: ArrayList<String>? = null
    private var isIssueClosed = false


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun attachView(view: InboxBaseView?) {
        mView = view as InboxDetailView
        reasonList = ArrayList(Arrays.asList(*mView?.getActivity()?.resources?.getStringArray(R.array.bad_reason_array)))
        ticketDetails
    }


    override fun detachView() {}
    override fun getSearchListener(): CustomEditText.Listener {
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == InboxBaseView.REQUEST_SUBMIT_FEEDBACK && resultCode == Activity.RESULT_OK) {
            submitCsatRating(data)
        }
    }

    private fun submitCsatRating(data: Intent?) {

        launchCatchError(
                block = {
                    mView?.showProgressBar()
                    val requestParams = submitRatingUseCase.createRequestParams(mView?.getCommentID(),
                            data?.extras?.getInt("emoji_state").toString(),
                            data?.getStringExtra("selected_items"))

                    val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)

                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {
                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0))
                    } else {
                        mView?.showMessage(mView?.getActivity()?.getString(R.string.cu_terima_kasih_atas_masukannya)
                                ?: "")
                        mView?.showIssueClosed()
                        isIssueClosed = true
                        Observable.timer(DELAY_FOUR_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Subscriber<Long>() {
                                    override fun onCompleted() {}
                                    override fun onError(e: Throwable) {}
                                    override fun onNext(aLong: Long) {
                                        ticketDetails
                                    }
                                })
                    }

                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )

    }

    override fun onDestroy() {}
    override fun getBottomFragment(resID: Int): BottomSheetDialogFragment {
        val bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID)
        bottomFragment.setPresenter(this)
        return bottomFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_search) {
            mView?.toggleSearch(View.VISIBLE)
            true
        } else if (item.itemId == android.R.id.home) {
            if (mView?.isSearchMode() == true) {
                mView?.toggleSearch(View.GONE)
                if (mTicketDetail?.isShowRating == true) {
                    mView?.toggleTextToolbar(View.GONE)
                } else if (mTicketDetail?.status.equals(getUtils()?.CLOSED, ignoreCase = true)
                        && mTicketDetail?.isShowRating != true) {
                    mView?.showIssueClosed()
                } else {
                    mView?.toggleTextToolbar(View.VISIBLE)
                }
                mView?.clearSearch()
                mView?.exitSearchMode()
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun reAttachView() {}
    override fun clickCloseSearch() {}
    override fun refreshLayout() {
        ticketDetails
    }

    override fun onSearchSubmitted(text: String) {
        var mText = text
        mText = mText.trim { it <= ' ' }
        if (mText.isNotEmpty()) {
            search(mText)
        } else {
            searchIndices?.clear()
        }
    }

    override fun onSearchTextChanged(text: String) {
        var mText = text
        mText = mText.trim { it <= ' ' }
        if (mText.isNotEmpty()) {
            search(mText)
        } else {
            searchIndices?.clear()
        }
    }

    private val ticketDetails: Unit
        get() {
            mView?.showProgressBar()
            var tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID)
            if (tId == null) {
                tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_ID)
            }
            launchCatchError(
                    block = {
                        val requestParams = inboxOptionUseCase.createRequestParams(tId)
                        val chipGetInboxDetail = inboxOptionUseCase.getChipInboxDetail(requestParams)
                        if (chipGetInboxDetail?.data?.isSuccess == 1) {
                            mTicketDetail = chipGetInboxDetail.data?.tickets
                            val topItem = CommentsItem()
                            topItem.attachment = mTicketDetail?.attachment
                            topItem.message = mTicketDetail?.message
                            topItem.messagePlaintext = mTicketDetail?.message
                            topItem.createdBy = mTicketDetail?.createdBy
                            topItem.createTime = mTicketDetail?.createTime
                            var commentsItems = mTicketDetail?.comments
                            if (commentsItems == null) {
                                commentsItems = ArrayList()
                                mTicketDetail?.comments = commentsItems
                                commentsItems.add(topItem)
                            } else {
                                commentsItems.add(0, topItem)
                            }
                            for (item in commentsItems) {
                                if (userData == null) {
                                    if (item.createdBy?.role == "customer") {
                                        userData = item.createdBy
                                    }
                                }
                                val createTime = getUtils()?.getDateTime(item.createTime)
                                item.createTime = createTime
                                var count = 0
                                var i = 0
                                while (i < createTime?.length ?: 0) {
                                    val c = createTime?.get(i)
                                    if (c == ' ') {
                                        count++
                                        if (count == 2) break
                                    }
                                    i++
                                }
                                item.shortTime = createTime?.substring(0, i)
                            }
                            if (isIssueClosed) {
                                mTicketDetail?.isShowRating = false
                                isIssueClosed = false
                            }
                            mTicketDetail?.let { mView?.renderMessageList(it) }
                            mView?.hideProgressBar()
                        } else if (chipGetInboxDetail?.data?.isSuccess == 0) {
                            mView?.showNoTicketView(chipGetInboxDetail.messageError)
                        }


                    },
                    onError = {
                        it.printStackTrace()
                    }
            )
        }

    private fun getFileUploaded(attachment: List<ImageUpload>): String {
        val reviewPhotos = JSONObject()
        try {
            for (image in attachment) {
                reviewPhotos.put(image.imageId, image.picObj)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return reviewPhotos.toString()
    }

    private fun fileSizeValid(fileLoc: String): Boolean {
        val file = File(fileLoc)
        val size = file.length()
        return size / 1024 < 10240
    }

    private fun isBitmapDimenValid(fileLoc: String): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(fileLoc).absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        return !(imageHeight < 300 && imageWidth < 300)
    }

    override fun onImageSelect(image: ImageUpload) {
        if (!fileSizeValid(image.fileLoc)) {
            showErrorMessage(MESSAGE_WRONG_FILE_SIZE)
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError1)
        } else if (!isBitmapDimenValid(image.fileLoc)) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION)
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError2)
        } else {
            mView?.addImage(image)
        }
    }

    private fun showErrorMessage(messageWrongParam: Int) {
        if (messageWrongParam == MESSAGE_WRONG_FILE_SIZE) {
            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.error_msg_wrong_size)
                    ?: "", true)
        } else if (messageWrongParam == MESSAGE_WRONG_DIMENSION) {
            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.error_msg_wrong_height_width)
                    ?: "", true)
        }
    }

    override fun watcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= 15) {
                    mView?.setSubmitButtonEnabled(true)
                } else {
                    mView?.setSubmitButtonEnabled(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    override fun sendMessage() {
        mView?.showSendProgress()
        val isValid = isUploadImageValid
        when {
            isValid >= 1 -> {
                val uploadedImage = mView?.getActivity()?.let {
                    uploadFile(it, mView?.imageList ?: listOf())
                }
                uploadedImage?.flatMap { imageUploads: List<ImageUpload> ->
                    var attachmentString = StringBuilder()
                    for (imageUpload in imageUploads) {
                        if (imageUpload != null) {
                            attachmentString.append("~").append(imageUpload.imageId)
                        }
                    }
                    attachmentString = StringBuilder(attachmentString.toString().replace("~~", "~"))
                    if (attachmentString.isNotEmpty()) attachmentString = StringBuilder(attachmentString.substring(1))
                    postMessageUseCase.setQueryMap(mTicketDetail?.id,
                            mView?.userMessage, 1, attachmentString.toString())
                    fileUploaded = getFileUploaded(imageUploads)
                    postMessageUseCase.createObservable(RequestParams.create()).subscribeOn(Schedulers.io())
                }?.flatMap { typeRestResponseMap: Map<Type, RestResponse> ->
                    val token = object : TypeToken<InboxDataResponse<CreateTicketResult?>?>() {}.type
                    val res1 = typeRestResponseMap[token]
                    val ticketListResponse: InboxDataResponse<*> = res1?.getData() as InboxDataResponse<*>
                    val createTicket = ticketListResponse.data as CreateTicketResult
                    if (createTicket.isSuccess > 0) {
                        if (createTicket.postKey != null && createTicket.postKey.length > 0) {
                            postMessageUseCase2.setQueryMap(fileUploaded, createTicket.postKey)
                            return@flatMap postMessageUseCase2.createObservable(RequestParams.create())
                        } else {
                            error = ticketListResponse.errorMessage?.get(0) as String
                            return@flatMap Observable.just<Map<Type, RestResponse>?>(null)
                        }
                    } else {
                        error = ticketListResponse.errorMessage?.get(0) as String
                        return@flatMap Observable.just<Map<Type, RestResponse>?>(null)
                    }
                }?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object : Subscriber<Map<Type, RestResponse>>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {
                        mView?.hideSendProgress()
                        mView?.setSnackBarErrorMessage(error ?: "", true)
                        e.printStackTrace()
                    }

                    override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                        mView?.hideSendProgress()
                        if (typeRestResponseMap.isNotEmpty()) {
                            val token = object : TypeToken<InboxDataResponse<StepTwoResponse?>?>() {}.type
                            val res1 = typeRestResponseMap[token]
                            val responseStep2: InboxDataResponse<*> = res1?.getData() as InboxDataResponse<*>
                            val stepTwoResponse = responseStep2.data as StepTwoResponse
                            if (stepTwoResponse.isSuccess > 0) {
                                addNewLocalComment()
                            } else {
                                mView?.setSnackBarErrorMessage(responseStep2.errorMessage?.get(0) as String, true)
                            }
                        } else {
                            mView?.setSnackBarErrorMessage(error ?: "", true)
                        }
                    }
                })
            }
            isValid == 0 -> {
                postMessageUseCase.setQueryMap(mTicketDetail?.id, mView?.userMessage, 0, "")
                postMessageUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {
                        mView?.hideSendProgress()
                    }

                    override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                        mView?.hideSendProgress()
                        val token = object : TypeToken<InboxDataResponse<CreateTicketResult?>?>() {}.type
                        val res1 = typeRestResponseMap[token]
                        val ticketListResponse: InboxDataResponse<*> = res1?.getData() as InboxDataResponse<*>
                        val createTicket = ticketListResponse.data as CreateTicketResult
                        if (createTicket.isSuccess > 0) {
                            addNewLocalComment()
                        } else {
                            mView?.setSnackBarErrorMessage(ticketListResponse.errorMessage?.get(0) as String, true)
                        }
                    }
                })
            }
            isValid == -1 -> {
                mView?.setSnackBarErrorMessage(mView?.getActivity()?.resources?.getString(R.string.invalid_images)
                        ?: "", true)
            }
        }
    }

    override fun clickRate(id: Int, commentID: String) { //        rateCommentID = commentID;
//        if (id == com.tokopedia.inbox.R.id.btn_yes) {
//            String YES = "YES";
//            postRatingUseCase.setQueryMap(rateCommentID, YES, 0, 0, "");
//            mView.showProgressBar();
//            sendRating();
//        } else {
//            if (mTicketDetail.isShowBadCSATReason()) {
//                mView.showBottomFragment();
//            } else {
//                postRatingUseCase.setQueryMap(rateCommentID, NO, 0, 0, "");
//                mView.showProgressBar();
//                mView.toggleTextToolbar(View.VISIBLE);
//                sendRating();
//            }
//        }
    }

    override fun setBadRating(position: Int) {
        if (position > 0 && position < 7) {
            postRatingUseCase.setQueryMap(rateCommentID, NO, 1, position, "")
            mView?.showProgressBar()
            mView?.toggleTextToolbar(View.VISIBLE)
            sendRating()
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickReason,
                    reasonList?.get(position - 1))
        }
    }

    override fun sendCustomReason(customReason: String) {
        mView?.showSendProgress()
        postRatingUseCase.setQueryMap(rateCommentID, NO, 1, 7, customReason)
        sendRating()
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickReason,
                customReason)
    }

    override fun getNextResult(): Int {
        return if (searchIndices != null && searchIndices?.size ?: 0 > 0) {
            if (next >= -1 && next < searchIndices?.size?.minus(1) ?: 0) {
                mView?.setCurrentRes(++next + 1)
                searchIndices?.get(next) ?: 0
            } else {
                mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.cu_no_more_results)
                        ?: "")
                -1
            }
        } else {
            mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.no_search_result)
                    ?: "")
            -1
        }
    }

    override fun getPreviousResult(): Int {
        return if (searchIndices != null && searchIndices?.size ?: 0 > 0) {
            if (next > -1 && next < searchIndices?.size ?: 0) {
                mView?.setCurrentRes(next + 1)
                searchIndices?.get(next--) ?: 0
            } else {
                mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.cu_no_more_results)
                        ?: "")
                -1
            }
        } else {
            mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.no_search_result)
                    ?: "")
            -1
        }
    }

    private fun uploadFile(context: Context, imageUploads: List<ImageUpload>): Observable<List<ImageUpload>> {
        return Observable
                .from(imageUploads)
                .flatMap { imageUpload: ImageUpload ->
                    val uploadUrl = getUtils()?.UPLOAD_URL
                    val networkCalculator = NetworkCalculator(
                            NetworkConfig.POST, context,
                            uploadUrl)
                            .setIdentity()
                            .addParam(PARAM_IMAGE_ID, imageUpload.imageId)
                            .addParam(PARAM_WEB_SERVICE, "1")
                            .compileAllParam()
                            .finish()
                    val file: File = try {
                        ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.fileLoc))
                    } catch (e: IOException) {
                        throw RuntimeException(context.getString(R.string.contact_us_error_upload_image))
                    }
                    val userId = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[NetworkCalculator.USER_ID])
                    val deviceId = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[NetworkCalculator.DEVICE_ID])
                    val hash = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[NetworkCalculator.HASH])
                    val deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[NetworkCalculator.DEVICE_TIME])
                    val fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                            file)
                    val imageId = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[PARAM_IMAGE_ID])
                    val web_service = RequestBody.create(MediaType.parse("text/plain"),
                            networkCalculator.content[PARAM_WEB_SERVICE])
                    val upload: Observable<ImageUploadResult>
                    upload = if (SessionHandler.isV4Login(context)) {
                        RetrofitUtils.createRetrofit(uploadUrl)
                                .create(UploadImageContactUsParam::class.java)
                                .uploadImage(
                                        networkCalculator.header[NetworkCalculator.CONTENT_MD5],  // 1
                                        networkCalculator.header[NetworkCalculator.DATE],  // 2
                                        networkCalculator.header[NetworkCalculator.AUTHORIZATION],  // 3
                                        networkCalculator.header[NetworkCalculator.X_METHOD],  // 4
                                        userId,
                                        deviceId,
                                        hash,
                                        deviceTime,
                                        fileToUpload,
                                        imageId, web_service
                                )
                    } else {
                        RetrofitUtils.createRetrofit(uploadUrl)
                                .create(UploadImageContactUsParam::class.java)
                                .uploadImagePublic(
                                        networkCalculator.header[NetworkCalculator.CONTENT_MD5],  // 1
                                        networkCalculator.header[NetworkCalculator.DATE],  // 2
                                        networkCalculator.header[NetworkCalculator.AUTHORIZATION],  // 3
                                        networkCalculator.header[NetworkCalculator.X_METHOD],  // 4
                                        userId,
                                        deviceId,
                                        hash,
                                        deviceTime,
                                        fileToUpload,
                                        imageId,
                                        web_service)
                    }
                    Observable.zip(Observable.just(imageUpload), upload) { imageUpload1: ImageUpload, imageUploadResult: ImageUploadResult ->
                        if (imageUploadResult.data != null) {
                            imageUpload1.picSrc = imageUploadResult.data.picSrc
                            imageUpload1.picObj = imageUploadResult.data.picObj
                        } else if (imageUploadResult.messageError != null) {
                            throw RuntimeException(imageUploadResult.messageError)
                        }
                        imageUpload1
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
    }

    private val isUploadImageValid: Int
        get() {
            val uploadImageList = mView?.imageList
            if (mTicketDetail?.isNeedAttachment == true && (uploadImageList == null || uploadImageList.isEmpty())) {
                mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.attachment_required)
                        ?: "", true)
                mView?.hideSendProgress()
                ContactUsTracking.sendGTMInboxTicket("",
                        InboxTicketTracking.Category.EventInboxTicket,
                        InboxTicketTracking.Action.EventNotAttachImageRequired,
                        "")
                return -2
            }
            val numOfImages = uploadImageList?.size ?: 0
            if (numOfImages > 0) {
                var count = 0
                for (item in 0 until numOfImages) {
                    val image = uploadImageList?.get(item)
                    if (fileSizeValid(image?.fileLoc ?: "") && isBitmapDimenValid(image?.fileLoc
                                    ?: "")) {
                        count++
                    }
                }
                return if (numOfImages == count) {
                    numOfImages
                } else {
                    -1
                }
            } else if (numOfImages == 0) {
                return 0
            }
            return -1
        }

    private fun sendRating() {
        postRatingUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                mView?.hideProgressBar()
                mView?.hideSendProgress()
                val token = object : TypeToken<InboxDataResponse<RatingResponse?>?>() {}.type
                val res1 = typeRestResponseMap[token]
                val ticketListResponse: InboxDataResponse<*> = res1?.getData() as InboxDataResponse<*>
                val ratingResponse = ticketListResponse.data as RatingResponse
                if (ratingResponse.isSuccess > 0) {
                    mView?.hideBottomFragment()
                    mView?.showMessage(mView?.getActivity()?.getString(R.string.thanks_input) ?: "")
                    if (mTicketDetail?.status == getUtils()?.OPEN || mTicketDetail?.status == getUtils()?.SOLVED) {
                        mView?.toggleTextToolbar(View.VISIBLE)
                    } else {
                        mView?.showIssueClosed()
                        mView?.updateClosedStatus(mTicketDetail?.subject)
                    }
                } else {
                    mView?.setSnackBarErrorMessage(ticketListResponse.errorMessage?.get(0) as String, true)
                    mView?.toggleTextToolbar(View.GONE)
                }
            }
        })
    }

    private fun search(searchText: String) {
        mView?.showProgressBar()
        if (searchIndices == null) searchIndices = ArrayList()
        searchIndices?.clear()
        Observable.from(mTicketDetail?.comments).map(object : Func1<CommentsItem, Int> {
            private var count = -1
            override fun call(commentsItem: CommentsItem): Int {
                count++
                return if (utils?.containsIgnoreCase(commentsItem.messagePlaintext
                                ?: "", searchText) == true) {
                    count
                } else {
                    -1
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onCompleted() {
                        next = 0
                        mView?.hideProgressBar()
                        searchIndices?.size?.let { mView?.enterSearchMode(searchText, it) }
                        if (searchIndices?.size ?: 0 > 0) {
                            ContactUsTracking.sendGTMInboxTicket("",
                                    InboxTicketTracking.Category.EventInboxTicket,
                                    InboxTicketTracking.Action.EventClickSearchDetails,
                                    InboxTicketTracking.Label.GetResult)
                        } else {
                            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.no_search_result)
                                    ?: "", false)
                            ContactUsTracking.sendGTMInboxTicket("",
                                    InboxTicketTracking.Category.EventInboxTicket,
                                    InboxTicketTracking.Action.EventClickSearchDetails,
                                    InboxTicketTracking.Label.NoResult)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mView?.hideProgressBar()
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_search_error)
                                ?: "", false)
                        e.printStackTrace()
                    }

                    override fun onNext(integer: Int) {
                        if (integer != -1) {
                            searchIndices?.add(integer)
                        }
                    }
                })
    }

    override fun getUtils(): Utils? {
        if (utils == null) {
            utils = mView?.getActivity()?.applicationContext?.let { Utils(it) }
        }
        return utils
    }

    override fun showImagePreview(position: Int, imagesLoc: List<AttachmentItem>) {
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                InboxTicketTracking.Label.ImageAttached)
        val imagesURL = ArrayList<String>()
        for (item in imagesLoc) {
            if (item.url?.isNotEmpty() == true) imagesURL.add(item.url
                    ?: "") else imagesURL.add(item.thumbnail ?: "")
        }
        mView?.showImagePreview(position, imagesURL)
    }

    override fun onClickEmoji(number: Int) {
        sendGTMEventView()
        mView?.startActivityForResult(ContactUsProvideRatingActivity.getInstance(mView?.getActivity() as Context,
                number,
                mView?.getCommentID() ?: "",
                mTicketDetail?.badCsatReasonList ?: arrayListOf()), InboxBaseView.REQUEST_SUBMIT_FEEDBACK)
    }


    override fun onClick(agreed: Boolean, commentPosition: Int, commentId: String) {
        val rating = if (agreed) KEY_LIKED else KEY_DISLIKED
        launchCatchError(
                block = {
                    val requestParams = submitRatingUseCase.createRequestParams(commentId, rating.toString(), "-")
                    mView?.showProgressBar();
                    val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)
                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {

                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0));
                    } else {
                        mView?.onSuccessSubmitOfRating(rating, commentPosition);
                    }
                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun closeTicket() {
        mView?.showProgressBar()
        launchCatchError(
                block = {
                    mView?.hideProgressBar()
                    val requestParams = closeTicketByUserUseCase.createRequestParams(mView?.ticketID, "mobile")
                    val chipGetInboxDetail = closeTicketByUserUseCase.getChipInboxDetail(requestParams);
                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {
                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0))
                    } else {
                        mView?.OnSucessfullTicketClose()
                    }
                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun getTicketStatus(): String {
        return mTicketDetail?.status ?: ""
    }

    private fun addNewLocalComment() {
        val newItem = CommentsItem()
        newItem.createdBy = userData
        newItem.message = mView?.userMessage
        newItem.createTime = getUtils()?.dateTimeCurrent
        val uploadImageList = mView?.imageList
        val attachmentItems: MutableList<AttachmentItem> = ArrayList()
        if (uploadImageList != null && !uploadImageList.isEmpty()) {
            for (upload in uploadImageList) {
                val attachment = AttachmentItem()
                attachment.thumbnail = upload.fileLoc
                attachmentItems.add(attachment)
            }
            newItem.attachment = attachmentItems
        }
        mTicketDetail?.comments?.add(newItem)
        mTicketDetail?.isNeedAttachment = false
        mView?.updateAddComment()
    }

    private fun sendGTMEventView() {
        ContactUsTracking.sendGTMInboxTicket(InboxTicketTracking.Event.EventView,
                InboxTicketTracking.Category.EventHelpMessageInbox,
                InboxTicketTracking.Action.EventImpressionOnCsatRating,
                mView?.ticketID)
    }

    companion object {
        const val KEY_LIKED = 101
        const val KEY_DISLIKED = 102
        const val DELAY_FOUR_MILLIS = 4000
        private const val MESSAGE_WRONG_DIMENSION = 0
        private const val MESSAGE_WRONG_FILE_SIZE = 1
        private const val PARAM_IMAGE_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
    }

}