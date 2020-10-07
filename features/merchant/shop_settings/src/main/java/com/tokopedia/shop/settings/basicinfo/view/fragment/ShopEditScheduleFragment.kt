package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopSchedulePresenter
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.unifycomponents.Toaster
import io.hansel.pebbletracesdk.presets.UIPresets.findViewById
import kotlinx.android.synthetic.main.activity_shop_edit_schedule.*
import java.util.*
import javax.inject.Inject

class ShopEditScheduleFragment : Fragment(){

    @Inject
    lateinit var updateShopSchedulePresenter: UpdateShopSchedulePresenter

    private var progressDialog: ProgressDialog? = null

    private var selectedStartCloseUnixTimeMs: Long = 0
    private var selectedEndCloseUnixTimeMs: Long = 0

    private lateinit var shopBasicDataModel: ShopBasicDataModel
    private var isClosedNow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(ShopEditScheduleActivity.SAVED_SELECTED_START_DATE)
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(ShopEditScheduleActivity.SAVED_SELECTED_END_DATE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_shop_edit_schedule, container, false)
    }


}