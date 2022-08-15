package com.tokopedia.campaign.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.R
import com.tokopedia.campaign.databinding.FragmentBaseCampaignManageProductListBinding
import com.tokopedia.campaign.widget.BaseCampaignManageProductListDividerItemDecoration
import com.tokopedia.campaign.widget.WidgetCampaignLabelBulkApply
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class BaseCampaignManageProductListFragment<F : AdapterTypeFactory> :
    BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentBaseCampaignManageProductListBinding>()
    override fun getScreenName(): String =
        BaseCampaignManageProductListFragment::class.java.canonicalName.orEmpty()

    protected var rvProductList: RecyclerView? = null
    protected var containerButtonSubmit: ViewGroup? = null
    protected var buttonSubmit: UnifyButton? = null
    protected var labelBulkApply: WidgetCampaignLabelBulkApply? = null
    protected var headerUnify: HeaderUnify? = null
    protected var ticker: Ticker? = null
    protected var textTotalProduct: Typography? = null
    protected var adapter: BaseListAdapter<Visitable<*>, F>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =
            FragmentBaseCampaignManageProductListBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRvAdapter()
        setupUnifyHeaderBackClickListener()
        setupSubmitButtonClickListener()
        setupRecyclerView()
    }

    private fun setupUnifyHeaderBackClickListener() {
        headerUnify?.setNavigationOnClickListener {
            onBackArrowClicked()
        }
    }

    abstract fun onBackArrowClicked()

    private fun initRvAdapter() {
        adapter = createAdapterInstance()
    }

    abstract fun createAdapterInstance(): BaseListAdapter<Visitable<*>, F>

    private fun setupSubmitButtonClickListener() {
        buttonSubmit?.setOnClickListener { onSubmitButtonClicked() }
    }

    abstract fun onSubmitButtonClicked()

    private fun initView() {
        rvProductList = viewBinding?.rvProductList
        containerButtonSubmit = viewBinding?.containerButtonSubmit
        buttonSubmit = viewBinding?.buttonSubmit
        labelBulkApply = viewBinding?.labelBulkApply
        headerUnify = viewBinding?.headerUnify
        ticker = viewBinding?.ticker
        textTotalProduct = viewBinding?.textTotalProduct
    }

    private fun setupRecyclerView() {
        rvProductList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@BaseCampaignManageProductListFragment.adapter
            itemAnimator = null
            setDecoration()
        }
    }

    protected fun RecyclerView.setDecoration() {
        val dividerDrawable = MethodChecker.getDrawable(
            context,
            R.drawable.base_campaign_manage_product_list_bg_separator
        )
        val dividerItemDecoration =
            BaseCampaignManageProductListDividerItemDecoration(dividerDrawable)
        if (itemDecorationCount > 0)
            removeItemDecorationAt(0)
        addItemDecoration(dividerItemDecoration)
    }

    protected fun showButtonSubmit() {
        containerButtonSubmit?.show()
    }

    protected fun hideButtonSubmit() {
        containerButtonSubmit?.hide()
    }
}