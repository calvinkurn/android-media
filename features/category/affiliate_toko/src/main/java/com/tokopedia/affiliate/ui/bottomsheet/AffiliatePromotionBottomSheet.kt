package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliatePromotionBottomSheet : BottomSheetUnify(), ShareButtonInterface {
    private var contentView: View? = null
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(this))
    private var currentName: String? = null
    private var url: String? = null
    private var identifier: String? = null
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf(
            AffiliateShareVHViewModel("Instagram", IconUnify.INSTAGRAM),
            AffiliateShareVHViewModel("TikTok", IconUnify.TIKTOK),
            AffiliateShareVHViewModel("Youtube", IconUnify.YOUTUBE),
            AffiliateShareVHViewModel("Facebook", IconUnify.FACEBOOK),
            AffiliateShareVHViewModel("Twitter", IconUnify.TWITTER),
            AffiliateShareVHViewModel("Blog", IconUnify.GLOBE),
            AffiliateShareVHViewModel("Whatsapp", IconUnify.WHATSAPP),
            AffiliateShareVHViewModel("Line", IconUnify.LINE),
            AffiliateShareVHViewModel("Lainnya", null)
    )

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromotionBSViewModel: AffiliatePromotionBSViewModel

    companion object {
        private const val COPY_LABEL = "Tokopedia"
        private const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"
        private const val KEY_PRODUCT_IMAGE = "KEY_PRODUCT_IMAGE"
        private const val KEY_PRODUCT_URL = "KEY_PRODUCT_URL"
        private const val KEY_PRODUCT_IDENTIFIER = "KEY_PRODUCT_IDENTIFIER"

        fun newInstance(productName: String, productImage: String, productUrl: String, productIdentifier: String): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_PRODUCT_NAME, productName)
                    putString(KEY_PRODUCT_IMAGE, productImage)
                    putString(KEY_PRODUCT_URL, productUrl)
                    putString(KEY_PRODUCT_IDENTIFIER, productIdentifier)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        affiliatePromotionBSViewModel = ViewModelProviders.of(this, viewModelProvider).get(AffiliatePromotionBSViewModel::class.java)
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        setTitle(getString(R.string.affiliate_where_to_promote))
        contentView = View.inflate(context,
                R.layout.affiliate_promotion_bottom_sheet, null)

        contentView?.findViewById<RecyclerView>(R.id.share_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
        contentView?.run {
            arguments?.let { bundle ->
                findViewById<Typography>(R.id.product_name).text = bundle.getString(KEY_PRODUCT_NAME)
                ImageHandler.loadImageCover2(findViewById(R.id.product_image), bundle.getString(KEY_PRODUCT_IMAGE))
                url = bundle.getString(KEY_PRODUCT_URL, "")
                identifier = bundle.getString(KEY_PRODUCT_IDENTIFIER)
            }
            setObservers(this)
        }

        setChild(contentView)
    }

    private fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    private fun setObservers(contentView: View) {
        affiliatePromotionBSViewModel.generateLinkData().observe(this, {
            it.data.firstOrNull()?.let { data ->
                val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText(COPY_LABEL, data.url.short))
                Toaster.build(contentView.rootView, getString(R.string.affiliate_link_generated_succesfully, currentName),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        })

        affiliatePromotionBSViewModel.loading().observe(this, { isLoad ->
            if (isLoad != null) {
                loading(isLoad)
            }
        })

        affiliatePromotionBSViewModel.getErrorMessage().observe(this, { error ->
            if (error != null) {
                Toaster.build(contentView.rootView, error,
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun loading(stop: Boolean) {
        for (visitable in listVisitable) {
            val updateVisitable = visitable as AffiliateShareVHViewModel
            if (currentName == updateVisitable.name) {
                updateVisitable.buttonLoad = stop
                adapter.setElement(adapter.list.indexOf(visitable), updateVisitable)
                break
            }
        }
    }

    override fun onShareButtonClick(name: String?) {
        currentName = name
        affiliatePromotionBSViewModel.affiliateGenerateLink(name, url, identifier)
    }

}
