package com.tokopedia.shopetalasepicker.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shopetalasepicker.R
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.di.component.DaggerShopEtalaseComponent
import com.tokopedia.shopetalasepicker.di.component.ShopEtalaseComponent
import com.tokopedia.shopetalasepicker.di.module.ShopEtalaseModule
import com.tokopedia.shopetalasepicker.view.adapter.ShopEtalaseAdapterTypeFactory
import com.tokopedia.shopetalasepicker.view.listener.ShopEtalaseView
import com.tokopedia.shopetalasepicker.view.model.ShopEtalaseViewModel
import com.tokopedia.shopetalasepicker.view.presenter.ShopEtalasePresenter

import javax.inject.Inject

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalaseFragment : BaseListFragment<ShopEtalaseViewModel, ShopEtalaseAdapterTypeFactory>(), ShopEtalaseView, HasComponent<ShopEtalaseComponent> {

    private var shopId: String? = null

    @Inject
    lateinit var shopEtalasePresenter: ShopEtalasePresenter

    private var selectedEtalaseId: String? = null
    private var isShowDefault: Boolean = false
    private var isShowZeroProduct: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.run {
            shopId = getString(ShopParamConstant.EXTRA_SHOP_ID)
            selectedEtalaseId = getString(ShopParamConstant.EXTRA_ETALASE_ID)
            isShowDefault = getBoolean(ShopParamConstant.EXTRA_IS_SHOW_DEFAULT, false)
            isShowZeroProduct = getBoolean(ShopParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list_swipe, container, false)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onSwipeRefresh() {
        shopEtalasePresenter.clearEtalaseCache()
        super.onSwipeRefresh()
    }

    override fun loadData(i: Int) {
        shopEtalasePresenter.getShopEtalase(shopId, isShowDefault, isShowZeroProduct)
    }

    override fun getAdapterTypeFactory(): ShopEtalaseAdapterTypeFactory {
        return ShopEtalaseAdapterTypeFactory()
    }

    override fun onItemClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        activity?.run {
            val intent = Intent()
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, shopEtalaseViewModel.etalaseId)
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_NAME, shopEtalaseViewModel.etalaseName)
            intent.putExtra(ShopParamConstant.EXTRA_USE_ACE, shopEtalaseViewModel.isUseAce)
            intent.putExtra(ShopParamConstant.EXTRA_ETALASE_BADGE, shopEtalaseViewModel.etalaseBadge)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun renderList(list: List<ShopEtalaseViewModel>, isHasNext: Boolean) {
        if (selectedEtalaseId?.isNotEmpty() == true) {
            list.find { selectedEtalaseId!!.equals(it.etalaseId, true) }
                    ?.isSelected = true
        } else {
            list[DEFAULT_INDEX_SELECTION].isSelected = true
        }
        super.renderList(list, isHasNext)
    }

    override fun initInjector() {
        component.inject(this)
        shopEtalasePresenter.attachView(this)
    }

    override fun getComponent(): ShopEtalaseComponent {
        return DaggerShopEtalaseComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .shopEtalaseModule(ShopEtalaseModule())
                .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        shopEtalasePresenter.detachView()
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {
        val DEFAULT_INDEX_SELECTION = 0

        fun createInstance(shoId: String?, selectedEtalaseId: String?, isShowDefault: Boolean? = false,
                           isShowZeroProduct : Boolean? = false): ShopEtalaseFragment {
            val fragment = ShopEtalaseFragment()
            val arguments = Bundle()
            arguments.putString(ShopParamConstant.EXTRA_SHOP_ID, shoId)
            arguments.putString(ShopParamConstant.EXTRA_ETALASE_ID, selectedEtalaseId)
            arguments.putBoolean(ShopParamConstant.EXTRA_IS_SHOW_DEFAULT, isShowDefault ?: false)
            arguments.putBoolean(ShopParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, isShowZeroProduct ?: false)
            fragment.arguments = arguments
            return fragment
        }
    }


}