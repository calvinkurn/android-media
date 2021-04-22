package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ActionButtonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*

class BuyerOrderDetailFragment : BaseDaggerFragment(), ActionButtonClickListener {

    private val mockModel = BuyerOrderDetailUiModel(
            actionButtons = ActionButtonsUiModel(
                    primaryActionButton = ActionButtonsUiModel.ActionButton(
                            key = "cancel_order",
                            label = "Batalkan Pesanan"
                    ),
                    secondaryActionButtons = listOf(
                            ActionButtonsUiModel.ActionButton(
                                    key = "help",
                                    label = "Bantuan"
                            ),
                            ActionButtonsUiModel.ActionButton(
                                    key = "ask_seller",
                                    label = "Tanya Penjual"
                            )
                    )
            ))

    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this)
    }
    private val adapter: BaseAdapter<BuyerOrderDetailTypeFactory> by lazy {
        BaseAdapter(typeFactory)
    }
    private val secondaryActionButtonBottomSheet: SecondaryActionButtonBottomSheet by lazy {
        SecondaryActionButtonBottomSheet(requireContext(), this)
    }

    override fun getScreenName() = BuyerOrderDetailFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
    }

    private fun setupRecyclerView() {
        rvBuyerOrderDetail.adapter = adapter
        adapter.addElement(mockModel.actionButtons)
    }

    override fun onActionButtonClicked(key: String) {
        when (key) {
            ActionButtonsViewHolder.SECONDARY_ACTION_BUTTON_KEY -> onSecondaryActionButtonClicked()
            else -> {
                view?.let {
                    Toaster.build(it, key, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
                }
            }
        }
    }

    private fun onSecondaryActionButtonClicked() {
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(mockModel.actionButtons.secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }
}