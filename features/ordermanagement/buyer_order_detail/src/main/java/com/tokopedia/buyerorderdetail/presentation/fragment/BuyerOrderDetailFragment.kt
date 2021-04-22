package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ActionButtonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.BuyProtectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyProtectionUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThickDividerUiModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
import java.util.*

class BuyerOrderDetailFragment : BaseDaggerFragment(), ActionButtonClickListener, BuyProtectionViewHolder.BuyProtectionListener {

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
            ),
            buyProtectionUiModel = BuyProtectionUiModel(
                    title = "Beli Proteksi?",
                    description = "12 bulan proteksi diluar cakupan garansi resmi, ganti rugi hingga senilai harga barang",
                    deadline = Calendar.getInstance(TimeZone.getDefault()).apply {
                        add(Calendar.SECOND, 15)
                    }.timeInMillis
            ))

    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this, this)
    }
    private val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
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

    override fun onClickBuyProtection() {
        view?.let {
            Toaster.build(it, "Beli proteksi dong...", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onReachBuyProtectionDeadline() {
        adapter.removeBuyProtectionWidget()
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
        adapter.addElement(ThickDividerUiModel())
        adapter.addElement(mockModel.buyProtectionUiModel)
        adapter.addElement(ThickDividerUiModel())
        adapter.addElement(mockModel.actionButtons)
    }

    private fun onSecondaryActionButtonClicked() {
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(mockModel.actionButtons.secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }
}