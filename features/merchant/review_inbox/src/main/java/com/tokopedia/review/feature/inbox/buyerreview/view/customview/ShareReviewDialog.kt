package com.tokopedia.review.feature.inbox.buyerreview.view.customview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.review.common.util.ClipboardHandler
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ShareAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ShareModel
import com.tokopedia.review.feature.inbox.buyerreview.view.widgets.ShareItem
import com.tokopedia.review.inbox.R

/**
 * Created by stevenfredian on 2/20/17.
 */
class ShareReviewDialog constructor(
    private val context: Context,
    private val callbackManager: CallbackManager,
    private val fragment: Fragment
) {
    companion object {
        const val FACEBOOK_ICON_URL: String =
            "https://images.tokopedia.net/img/android/review/review_ic_facebook_share.png"
        const val LINK_ICON_URL: String =
            "https://images.tokopedia.net/img/android/review/review_ic_copy_share.png"
    }

    @SuppressLint("UnifyComponentUsage")
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private val appGrid: GridView?
    private val cancelButton: View?
    private var adapterRead = ArrayAdapter.createFromResource(
        context,
        R.array.talk_read,
        R.layout.reputation_dialog_item
    )
    private val adapter = ShareAdapter(context)
    private var model: ShareModel? = null

    init {
        dialog.setContentView(R.layout.reputation_share_review_dialog)
        appGrid = dialog.findViewById<View>(R.id.grid) as GridView?
        cancelButton = dialog.findViewById(R.id.cancel_but)
        initAdapters()
        setListener()
    }

    fun show() {
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

    fun setModel(model: ShareModel) {
        this.model = model
    }

    private fun initAdapters() {
        adapterRead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.addItem(ShareItem(FACEBOOK_ICON_URL, "Facebook", shareFb()))
        adapter.addItem(ShareItem(LINK_ICON_URL, "Copy Link", shareCopyLink()))
        appGrid?.adapter = adapter
    }

    private fun setListener() {
        cancelButton?.setOnClickListener { dismissDialog() }
    }

    private fun shareCopyLink(): View.OnClickListener {
        return View.OnClickListener {
            dismissDialog()
            ClipboardHandler.CopyToClipboard(context as Activity?, model?.link)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareFb(): View.OnClickListener {
        return View.OnClickListener {
            dismissDialog()
            val shareDialog = ShareDialog(fragment)
            shareDialog.apply {
                registerCallback(
                    callbackManager,
                    object : FacebookCallback<Sharer.Result?> {
                        override fun onSuccess(result: Sharer.Result?) {
                            SnackbarManager.make(
                                fragment.activity,
                                context.getString(R.string.success_share_review),
                                Snackbar.LENGTH_LONG
                            ).show()
                            dismissDialog()
                        }

                        override fun onCancel() {
                            Log.i("facebook", "onCancel")
                        }

                        override fun onError(error: FacebookException) {
                            Log.i("facebook", "onError: " + error)
                            SnackbarManager.make(
                                fragment.activity,
                                context.getString(R.string.error_share_review),
                                Snackbar.LENGTH_LONG
                            ).show()
                            dismissDialog()
                        }
                    })
                if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                    val builder: ShareLinkContent.Builder = ShareLinkContent.Builder()
                    if (model?.title != "") builder.setContentTitle(
                        model?.title
                    )
                    if (model?.content != "") builder.setQuote(
                        model?.content
                    )
                    if (model?.image != "") builder.setImageUrl(
                        Uri.parse(model?.image)
                    )
                    if (model?.link != "") builder.setContentUrl(
                        Uri.parse(model?.link)
                    )
                    val linkContent: ShareLinkContent = builder.build()
                    show(linkContent)
                }
            }
        }
    }
}