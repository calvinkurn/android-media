package com.tokopedia.review.feature.inbox.buyerreview.view.customview

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
    private val context: Context?,
    private val callbackManager: CallbackManager,
    fragment: Fragment
) {
    private val fragment: Fragment
    private val dialog: BottomSheetDialog
    private val appGrid: GridView?
    private val cancelButton: View?
    private var adapterRead: ArrayAdapter<CharSequence>? = null
    private var shareDialog: ShareDialog? = null
    private var adapter: ShareAdapter? = null
    private var model: ShareModel? = null
    fun initAdapter() {
        adapterRead = ArrayAdapter.createFromResource(
            (context)!!,
            R.array.talk_read,
            R.layout.reputation_dialog_item
        )
        adapterRead!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    fun setAdapter() {
        adapter = ShareAdapter(context)
        adapter!!.addItem(ShareItem(FACEBOOK_ICON_URL, "Facebook", shareFb()))
        adapter!!.addItem(ShareItem(LINK_ICON_URL, "Copy Link", shareCopyLink()))
        appGrid!!.setAdapter(adapter)
    }

    fun setListener() {
        cancelButton!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                dismissDialog()
            }
        })
    }

    fun show() {
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

    private fun shareCopyLink(): View.OnClickListener {
        return object : View.OnClickListener {
            public override fun onClick(view: View) {
                dismissDialog()
                ClipboardHandler.CopyToClipboard(context as Activity?, model.getLink())
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareFb(): View.OnClickListener {
        return object : View.OnClickListener {
            public override fun onClick(view: View) {
                dismissDialog()
                shareDialog = ShareDialog(fragment)
                shareDialog!!.registerCallback(
                    callbackManager,
                    object : FacebookCallback<Sharer.Result?> {
                        public override fun onSuccess(result: Sharer.Result?) {
                            SnackbarManager.make(
                                fragment.getActivity(),
                                context!!.getString(R.string.success_share_review),
                                Snackbar.LENGTH_LONG
                            ).show()
                            dismissDialog()
                        }

                        public override fun onCancel() {
                            Log.i("facebook", "onCancel")
                        }

                        public override fun onError(error: FacebookException) {
                            Log.i("facebook", "onError: " + error)
                            SnackbarManager.make(
                                fragment.getActivity(),
                                context!!.getString(R.string.error_share_review),
                                Snackbar.LENGTH_LONG
                            ).show()
                            dismissDialog()
                        }
                    })
                if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                    val builder: ShareLinkContent.Builder = ShareLinkContent.Builder()
                    if (model.getTitle() != null && !(model.getTitle() == "")) builder.setContentTitle(
                        model.getTitle()
                    )
                    if (model.getContent() != null && !(model.getContent() == "")) builder.setQuote(
                        model.getContent()
                    )
                    if (model.getImage() != null && !(model.getImage() == "")) builder.setImageUrl(
                        Uri.parse(model.getImage())
                    )
                    if (model.getLink() != null && !(model.getLink() == "")) builder.setContentUrl(
                        Uri.parse(model.getLink())
                    )
                    val linkContent: ShareLinkContent = builder.build()
                    shareDialog!!.show(linkContent)
                }
            }
        }
    }

    fun setModel(model: ShareModel?) {
        this.model = model
    }

    companion object {
        val FACEBOOK_ICON_URL: String =
            "https://images.tokopedia.net/img/android/review/review_ic_facebook_share.png"
        val LINK_ICON_URL: String =
            "https://images.tokopedia.net/img/android/review/review_ic_copy_share.png"
    }

    init {
        dialog = BottomSheetDialog((context)!!)
        this.fragment = fragment
        dialog.setContentView(R.layout.reputation_share_review_dialog)
        appGrid = dialog.findViewById<View>(R.id.grid) as GridView?
        cancelButton = dialog.findViewById(R.id.cancel_but)
        initAdapter()
        setAdapter()
        setListener()
    }
}