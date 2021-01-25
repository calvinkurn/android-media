package com.tokopedia.mvcwidget

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.mvcwidget.views.MvcView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DummyMvcActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fm = FrameLayout(this)
        fm.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val mvcView = MvcView(this)
//        mvcView.setData(MvcData("Ada Gratis Ongkir Rp100.000",
//                "Ayo pakai promonya biar makin hemat!",
//        "https://ecs7.tokopedia.net/img/tokopoints/MVC/CouponIconWhite/coupon-1.png"),"480136")
        fm.addView(mvcView)
        setContentView(fm)

        GlobalScope.launch (Dispatchers.IO){
            val shopId = "480136"
            val usecase = MVCSummaryUseCase(GqlUseCaseWrapper())
            val params = usecase.getQueryParams(shopId)
            val response = usecase.getResponse(params)
            response?.data?.let {
                val mvcData = usecase.mapTokopointsCatalogMVCSummaryToMvcData(it)
                withContext(Dispatchers.Main){
                    mvcView.setData(mvcData, shopId)
                }
            }

        }

//        var bottomSheet = BottomSheetUnify()
//        val tokomemberView = MvcTokomemberBmView(this)
//        bottomSheet.setChild(tokomemberView)
//        bottomSheet.show(supportFragmentManager,"BottomSheet Tag")
//        val messages = arrayListOf("TokoMember adalah program loyalitas pelanggan dari brand dan Tokopedia dengan berbagai keuntungan\n" +
//                "berupa kupon, poin, dll.","Kamu bisa menemukan banyak toko yang berpartisipasi dengan TokoMember melalui halaman Akun, Rewards, Produk, dan juga Halaman Toko.",
//                "Selain mendapatkan berbagai keuntungan, kamu juga akan mendapat kartu khusus member setelah menjadi member.")
//        tokomemberView.setData(MvcTokomemberBmViewData(arrayListOf("","",""),messages,"Daftar Jadi Member"))

    }
}