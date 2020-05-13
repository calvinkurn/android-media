package com.tokopedia.hotel.cancellation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationButtonEnum
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity.Companion.EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation_confirmation.*
import javax.inject.Inject

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationViewModel: HotelCancellationViewModel

    lateinit var cancellationSubmitModel: HotelCancellationSubmitModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            cancellationSubmitModel = it.getParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA) ?: HotelCancellationSubmitModel()
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(cancellationSubmitModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_cancellation_confirmation, container, false)


    private fun initView(cancellationSubmitModel: HotelCancellationSubmitModel) {
        hotel_cancellation_confirmation_title.text = cancellationSubmitModel.title
        hotel_cancellation_confirmation_subtitle.text = cancellationSubmitModel.desc

        hotel_cancellation_confirmation_button_layout.removeAllViews()
        for (button in cancellationSubmitModel.actionButton) {
            hotel_cancellation_confirmation_button_layout.addView(getButtonFromType(button))
        }
    }

    private fun getButtonFromType(actionButton: HotelCancellationSubmitModel.ActionButton): UnifyButton {
        val button = UnifyButton(requireContext())
        button.buttonType = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonType
        button.buttonVariant = HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType).buttonVariant
        button.buttonSize = UnifyButton.Size.MEDIUM
        button.text = actionButton.label
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        button.setMargin(0, 0, 0, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
        button.setOnClickListener {
            if (HotelCancellationButtonEnum.getEnumFromValue(actionButton.buttonType)
                    == HotelCancellationButtonEnum.RETRYSUBMISSION) {
                // submit again
            } else {
                RouteManager.route(requireContext(), actionButton.uri)
            }
        }
        return button
    }

    companion object {
        fun getInstance(cancellationSubmitModel: HotelCancellationSubmitModel): HotelCancellationConfirmationFragment =
                HotelCancellationConfirmationFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA, cancellationSubmitModel)
                    }
                }
    }
}