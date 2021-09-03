package com.tokopedia.exploreCategory.ui.bottomsheet

import android.app.Dialog
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
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.adapter.AffiliateAdapter
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterFactory
import com.tokopedia.exploreCategory.di.AffiliateComponent
import com.tokopedia.exploreCategory.di.DaggerAffiliateComponent
import com.tokopedia.exploreCategory.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import java.util.*
import javax.inject.Inject

class AffiliatePromotionBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromotionBSViewModel: AffiliatePromotionBSViewModel

    companion object {

        fun newInstance(): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                arguments = Bundle().apply {
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
            adapter.setVisitables(ArrayList())
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
        contentView?.run {
            setObservers(this)
        }

        adapter.addShareOptions()
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

        })

        affiliatePromotionBSViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                } else {
                }
            }
        })

        affiliatePromotionBSViewModel.getErrorMessage().observe(this, { error ->
            if (error != null) {
                Toaster.build(contentView, error,
                        Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
            }
        })
    }

}
