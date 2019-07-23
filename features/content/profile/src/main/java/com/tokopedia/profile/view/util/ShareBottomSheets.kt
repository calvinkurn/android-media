package com.tokopedia.profile.view.util

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.profile.R
import com.tokopedia.profile.analytics.ProfileAnalytics
import com.tokopedia.profile.di.DaggerProfileComponent
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by yfsx on 17/05/19.
 */
class ShareBottomSheets: BottomSheets(), ShareAdapter.OnItemClickListener {
    val TITLE_EN = "Share"

    @Inject
    lateinit var profileAnalytics: ProfileAnalytics

    val KEY_ADDING = ".isAddingProduct"
    val KEY_ISOWNER = "isOwner"
    val KEY_PROFILEID = "profileId"
    val KEY_SHARE_PROFILE = "share_profile"

    private val PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker"
    private val PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"
    private val PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity"
    private val PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity"
    private val PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias"
    private val PACKAGENAME_INSTAGRAM = "com.instagram.android";

    private val ClassNameApplications = arrayOf(PACKAGENAME_WHATSAPP,
            PACKAGENAME_FACEBOOK,
            PACKAGENAME_LINE,
            PACKAGENAME_TWITTER,
            PACKAGENAME_GPLUS,
            PACKAGENAME_INSTAGRAM)

    companion object {
        val COPY = "Copy"
        val STATUS_FLAG = "STATUS_FLAG"
        val STATUS_RUNNING = 1
        val STATUS_DONE = 2
        val STATUS_ERROR = 3
        val MESSAGE_ERROR_FLAG = "MESSAGE_ERROR_FLAG"
        val BROADCAST_ADD_PRODUCT = "BROADCAST_ADD_PRODUCT";
        val PRODUCT_ID = "PRODUCT_ID"
        val NO_PRODUCT_ID = -1
        val INVALID_MESSAGE_ERROR = "default"
        val PRODUCT_NAME = "PRODUCT_NAME"
        val IMAGE_URI = "IMAGE_URI"
        val PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION"
        val PRODUCT_URI = "PRODUCT_URI"
        private val KEY_WHATSAPP = "whatsapp"
        private val KEY_LINE = "line"
        private val KEY_TWITTER = "twitter"
        private val KEY_FACEBOOK = "facebook"
        private val KEY_GOOGLE = "google"
        private val KEY_INSTAGRAM = "instagram"
        val KEY_INSTAGRAM_DIRECT = "Direct"
        val NAME_INSTAGRAM = "Instagram"
        val KEY_YOUTUBE = "youtube"
        val KEY_OTHER = "lainnya"
        val KEY_COPY = "salinlink"

        private val TYPE = "text/plain"
    }

    private lateinit var data: LinkerData
    private lateinit var adapter: ShareAdapter
    private var isAdding: Boolean = false
    private var isOwner = false
    private var profileId = ""
    private var isShareProfile = false

    fun newInstance(data: LinkerData, isAddingProduct: Boolean, isOwner: Boolean, profileId: String, isShareProfile: Boolean): ShareBottomSheets {
        val fragment = ShareBottomSheets()
        val bundle = Bundle()
        bundle.putParcelable(ShareBottomSheets::class.java.getName(), data)
        bundle.putBoolean(ShareBottomSheets::class.java.getName() + KEY_ADDING, isAddingProduct)
        bundle.putBoolean(KEY_ISOWNER, isOwner)
        bundle.putString(KEY_PROFILEID, profileId)
        bundle.putBoolean(KEY_SHARE_PROFILE, isShareProfile)
        fragment.setArguments(bundle)
        return fragment
    }

    fun show(fragmentManager: FragmentManager, data: LinkerData, isOwner: Boolean, profileId: String, isShareProfile: Boolean) {
        newInstance(data, false, isOwner, profileId, isShareProfile).show(fragmentManager, TITLE_EN)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_feed_share
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun title(): String {
        return data.ogTitle ?: getString(R.string.title_share)
    }

    override fun configView(parentView: View) {
        arguments?.let{
            data = it.getParcelable(ShareBottomSheets::class.java.getName())
            isAdding = it.getBoolean(ShareBottomSheets::class.java.getName() + KEY_ADDING, false)
            isOwner = it.getBoolean(KEY_ISOWNER, false)
            profileId = it.getString(KEY_PROFILEID, "")
            isShareProfile = it.getBoolean(KEY_SHARE_PROFILE, false)
        }
        super.configView(parentView)
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mLayoutError: LinearLayout
    private lateinit var mTextViewError: TextView

    override fun initView(view: View) {
        DaggerProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
        mRecyclerView = view.findViewById(R.id.recyclerview_bottomsheet)
        mProgressBar = view.findViewById(R.id.progressbar)
        mLayoutError = view.findViewById(R.id.layout_error)
        mTextViewError = view.findViewById(R.id.message_error)

        val mLayoutManager = LinearLayoutManager(getActivity())
        mRecyclerView.layoutManager = mLayoutManager

        broadcastAddProduct()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val btnClose = getDialog().findViewById<ImageView>(com.tokopedia.design.R.id.btn_close)
        btnClose.setOnClickListener { dismiss() }
    }

    private fun init() {
        val intent = getIntent("")
        activity?.let {
            val resolvedActivities = it.packageManager
                    .queryIntentActivities(intent, 0)
            if (!resolvedActivities.isEmpty()) {
                val showApplications: ArrayList<ResolveInfo> = validate(resolvedActivities)
//                showApplications.addAll(getInstagramApps()) //for next development
                adapter = ShareAdapter(showApplications, it
                        .getPackageManager())
                mRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(this)
            } else {
                return
            }
        }

    }

    private fun validate(resolvedActivities: List<ResolveInfo>): ArrayList<ResolveInfo> {
        val showApplications = ArrayList<ResolveInfo>()
        for (resolveInfo in resolvedActivities) {
            if (Arrays.asList(*ClassNameApplications)
                            .contains(resolveInfo.activityInfo.name)) {
                showApplications.add(resolveInfo)
            }
        }
        return showApplications
    }

    fun getInstagramApps(): ArrayList<ResolveInfo> {
        val showApplications = ArrayList<ResolveInfo>()
        val pm = activity!!.getPackageManager()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        // mainIntent.setType("image/*");
        val resolveInfos = pm.queryIntentActivities(shareIntent, 0) // returns all applications which can listen to the SEND Intent
        if (resolveInfos != null && !resolveInfos.isEmpty()) {
            for (info in resolveInfos) {
                if (Arrays.asList(*ClassNameApplications)
                                .contains(info.activityInfo.packageName)) {
                    showApplications.add(info)
                }
            }
        }
        return showApplications
    }

    override fun onItemClick(packageName: String) {
        when (packageName) {
            KEY_OTHER -> {
                actionMore(packageName)
            }
            KEY_COPY -> {
                actionCopy()
            }
            KEY_INSTAGRAM -> {

            }
            KEY_YOUTUBE -> {

            }
            else -> {
                actionShare(packageName)
            }

        }
    }

    private fun actionCopy() {
        data.setSource(COPY)
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                activity?.let {
                                    ClipboardHandler().CopyToClipboard(it, data.originalTextContent)
                                }
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        })
        )

        Toast.makeText(getActivity(), getString(R.string.msg_copy), Toast.LENGTH_SHORT).show()
    }

    private fun actionShare(packageName: String) {
        val media = constantMedia(packageName)
        data.setSource(media)
        activity?.let {
            ShareSocmedHandler(it).ShareData(it, packageName,
                    TYPE, data.originalTextContent, "", null , "")
            sendTracker(packageName)
        }
    }

    private fun actionMore(packageName: String) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                        0, DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                val intent = getIntent(data.originalTextContent)
                                startActivity(Intent.createChooser(intent, getString(R.string.other)))
                                sendTracker(packageName)
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        }
                )
        )
    }

    private fun getIntent(contains: String): Intent {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = TYPE

        mIntent.putExtra(Intent.EXTRA_TITLE, data.getName())
        mIntent.putExtra(Intent.EXTRA_SUBJECT, data.getName())
        mIntent.putExtra(Intent.EXTRA_TEXT, contains)
        return mIntent
    }

    private var addProductReceiver: BroadcastReceiver? = null

    private fun stateProgress(progress: Boolean) {
        mLayoutError.visibility = View.GONE
        if (progress) {
            mProgressBar.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        } else {
            mProgressBar.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
            init()
        }
    }

    private fun broadcastAddProduct() {
        stateProgress(isAdding)
        addProductReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    val status = bundle.getInt(STATUS_FLAG, STATUS_ERROR)
                    when (status) {
                        STATUS_DONE -> {
                            setData(bundle)
                            stateProgress(false)
                        }
                        STATUS_ERROR -> {
                            stateProgress(false)
                            onError(bundle)
                        }
                        else -> {
                            stateProgress(false)
                            onError(bundle)
                        }
                    }
                }
            }
        }
    }

    fun onError(resultData: Bundle) {
        val messageError = resultData.getString(MESSAGE_ERROR_FLAG)
        mProgressBar.visibility = View.GONE
        mLayoutError.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        mTextViewError.text = messageError + "\n" + getString(R.string.error_failed_add_product)
    }

    fun setData(data: Bundle) {
        this.data = LinkerData()
        this.data.setType(LinkerData.PRODUCT_TYPE)
        this.data.setName(data.getString(PRODUCT_NAME))
        val imageUri = data.getString(IMAGE_URI)
        this.data.setImgUri(data.getString(IMAGE_URI))
        this.data.setDescription(data.getString(PRODUCT_DESCRIPTION))
        this.data.setUri(data.getString(PRODUCT_URI))
        this.data.setId(data.getString(PRODUCT_ID))
    }

    override fun onResume() {
        super.onResume()
        activity!!.registerReceiver(addProductReceiver,
                IntentFilter(BROADCAST_ADD_PRODUCT))
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(addProductReceiver)
    }

    /**
     * Tracking
     * @param packageName
     * @return String media tracking
     */
    private fun constantMedia(packageName: String): String {
        return when {
            packageName.contains(KEY_WHATSAPP) -> KEY_WHATSAPP
            packageName.contains(KEY_LINE) -> KEY_LINE
            packageName.contains(KEY_TWITTER) -> KEY_TWITTER
            packageName.contains(KEY_FACEBOOK) -> KEY_FACEBOOK
            packageName.contains(KEY_GOOGLE) -> KEY_GOOGLE
            packageName.contains(KEY_OTHER) -> KEY_OTHER
            else -> ""
        }
    }

    private fun sendTracker(packageName: String) {
        if (isShareProfile)
            profileAnalytics.eventClickShareProfileOpsiIni(isOwner, profileId, constantMedia(packageName))
        else
            profileAnalytics.eventClickSharePostOpsiIni(isOwner, profileId, constantMedia(packageName))
    }
}