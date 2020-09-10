package com.tokopedia.activation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.activation.R
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class ActivationPageFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ActivationPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ActivationPageViewModel::class.java]
    }

    private var codIcon: ImageUnify? = null
    private var codTitleText: Typography? = null
    private var codDescText: Typography? = null
    private var codImageExtend: ImageUnify? = null

    private var codAdvantageTitle: Typography? = null
    private var codAdvantageDesc: Typography? = null

    private var codTncTitle: Typography? = null

    private var codTncImage1: ImageUnify? = null
    private var codTncDesc1: Typography? = null

    private var codTncImage2: ImageUnify? = null
    private var codTncDesc2: Typography? = null

    private var codTncImage3: ImageUnify? = null
    private var codTncDesc3: Typography? = null

    private var codSeeMore: Typography? = null

    private var codCheckbox: CheckboxUnify? = null
    private var codCheckboxTnc: Typography? = null

    private var codButtonSave: UnifyButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_activation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        getShopFeature()
    }

    private fun initViews() {
        codIcon = view?.findViewById(R.id.cod_icon)
        codTitleText = view?.findViewById(R.id.cod_title_text)
        codDescText = view?.findViewById(R.id.cod_desc_text)
        codImageExtend = view?.findViewById(R.id.cod_layout_extend)

        codAdvantageTitle = view?.findViewById(R.id.advantage_title_cod)
        codAdvantageDesc = view?.findViewById(R.id.advantage_desc_cod)

        codTncTitle = view?.findViewById(R.id.tnc_title_cod)

        codTncImage1 = view?.findViewById(R.id.check_icon_1)
        codTncDesc1 = view?.findViewById(R.id.tnc_decs_1)

        codTncImage2 = view?.findViewById(R.id.check_icon_2)
        codTncDesc2 = view?.findViewById(R.id.tnc_decs_2)

        codTncImage3 = view?.findViewById(R.id.check_icon_3)
        codTncDesc3 = view?.findViewById(R.id.tnc_decs_3)

        codSeeMore = view?.findViewById(R.id.see_more)

        codCheckbox = view?.findViewById(R.id.cb_tnc)
        codCheckboxTnc = view?.findViewById(R.id.checkbox_tnc)

        codButtonSave = view?.findViewById(R.id.btn_save)
    }

    private fun initViewModel() {
        viewModel.shopFeature.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ActivationPageState.Success -> {

                }

                is ActivationPageState.Fail -> {

                }

                is ActivationPageState.Loading -> {

                }
            }
        })
    }

    private fun getShopFeature() {
        //ToDo: shopId here
        viewModel.getShopFeature("//ToDO: shopId here")
    }

    private fun updateShopFeature() {
        //ToDo: value here
        viewModel.updateShopFeature(true)
    }



}