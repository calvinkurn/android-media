package com.tokopedia.shop.common.view.bottomsheet

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.bottomsheet.adapter.ShopShareBottomSheetAdapter
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopShareModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Rafli Syam 20/07/2020
 */
class ShopShareBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.shop_page_share_bottomsheet
        private val TAG = ShopShareBottomSheet::class.java.simpleName
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
        private const val PACKAGE_NAME_INSTAGRAM = "com.instagram.android"
        private const val PACKAGE_NAME_FACEBOOK = "com.facebook.katana"
        private const val PACKAGE_NAME_WHATSAPP = "com.whatsapp"
        private const val PACKAGE_NAME_LINE = "jp.naver.line.android"
        private const val PACKAGE_NAME_TWITTER = "com.twitter.android"
        private const val PACKAGE_NAME_TELEGRAM = "org.telegram.messenger"

        fun createInstance(): ShopShareBottomSheet = ShopShareBottomSheet()
    }

    enum class MimeType(val type: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE)
    }

    private var bottomSheetListener: ShopShareBottomsheetListener? = null
    private var rvSocialMediaList: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun init(bottomSheetListener: ShopShareBottomsheetListener) {
        this.bottomSheetListener = bottomSheetListener
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }

    private fun setupBottomSheetChildView(inflater: LayoutInflater, container: ViewGroup?) {
        inflater.inflate(LAYOUT, container).apply {
            rvSocialMediaList = findViewById(R.id.rv_social_media_list)
            setTitle(context.getString(R.string.shop_page_share_to_social_media_text))
            setChild(this)
            setCloseClickListener {
                bottomSheetListener?.onCloseBottomSheet()
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvSocialMediaList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = bottomSheetListener?.let { ShopShareBottomSheetAdapter(context, it, generateSocialMediaList(context)) }
        }
    }

    private fun getResolvedActivity(context: Context?, intent: Intent?): ResolveInfo? = context?.let { ctx ->
        intent?.let {
            return ctx.packageManager.resolveActivity(it, 0)
        }
    }

    private fun getActivityIcon(context: Context?, intent: Intent?): Drawable? {
        getResolvedActivity(context, intent).let {
            return it?.loadIcon(context?.packageManager)
        }
    }

    private fun getAppIntent(type: MimeType, packageName: String?): Intent {
        val intentType = if(type == MimeType.IMAGE) {
            MimeType.IMAGE.type
        } else {
            MimeType.TEXT.type
        }
        return Intent(Intent.ACTION_SEND).apply {
            setType(intentType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(packageName)
        }
    }

    private fun generateSocialMediaList(context: Context?): List<ShopShareModel> {
        return mutableListOf(
                ShopShareModel.CopyLink().apply {
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_copy_link)
                    socialMediaIcon = context?.getResDrawable(R.drawable.ic_copy_link)
                },
                ShopShareModel.Instagram().apply {
                    packageName = PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_instagram)
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Facebook().apply {
                    packageName = PACKAGE_NAME_FACEBOOK
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_facebook)
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Whatsapp().apply {
                    packageName = PACKAGE_NAME_WHATSAPP
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_whatsapp)
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Line().apply {
                    packageName = PACKAGE_NAME_LINE
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_line)
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Twitter().apply {
                    packageName = PACKAGE_NAME_TWITTER
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_twitter)
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Telegram().apply {
                    packageName = PACKAGE_NAME_TELEGRAM
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_telegram)
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                    socialMediaIcon = getActivityIcon(context, appIntent)
                },
                ShopShareModel.Others().apply {
                    socialMediaName = context?.resources?.getString(R.string.shop_page_share_action_more)
                    socialMediaIcon = context?.getResDrawable(R.drawable.ic_share_action_more)
                }
        ).filterNot {
            (it.packageName!!.isNotEmpty() && it.appIntent != null && getResolvedActivity(context, it.appIntent) == null)
        }
    }

}