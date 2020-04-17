package com.tokopedia.vouchercreation.voucherlist.view.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getBooleanArgs
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.voucherlist.model.BaseVoucherListUiModel
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment : BaseListFragment<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl>() {

    companion object {
        private const val KEY_IS_ACTIVE_VOUCHER = "is_active_voucher"

        fun newInstance(isActiveVoucher: Boolean): VoucherListFragment {
            return VoucherListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_IS_ACTIVE_VOUCHER, isActiveVoucher)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: VoucherListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(VoucherListViewModel::class.java)
    }

    private val isActiveVoucher by lazy { getBooleanArgs(KEY_IS_ACTIVE_VOUCHER, true) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hasOptionsMenu()

        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_mvc_voucher_active_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVoucherList

    override fun getAdapterTypeFactory(): VoucherListAdapterFactoryImpl {
        return VoucherListAdapterFactoryImpl(isActiveVoucher)
    }

    override fun getScreenName(): String = VoucherListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: BaseVoucherListUiModel?) {

    }

    override fun loadData(page: Int) {

    }

    private fun setupView() {
        showDummyData()
    }

    private fun showDummyData() {
        renderList(getDummyData())
    }

    private fun getDummyData(): List<VoucherUiModel> {
        val list = mutableListOf<VoucherUiModel>()
        repeat(10) {
            list.add(VoucherUiModel("x"))
        }
        return list
    }
}