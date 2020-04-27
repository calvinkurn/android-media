package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.topads.edit.data.response.ResponseGroupValidateName
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.activity.EditFormAdActivityCallback
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.activity_edit_form_ad.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.lang.NumberFormatException
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.lifecycle.Observer
import com.tokopedia.topads.edit.data.response.GroupInfoResponse
import timber.log.Timber
import javax.inject.Inject


class EditGroupAdFragment : BaseDaggerFragment() {

    private var btnState: Boolean = true
    private var callback: EditFormAdActivityCallback? = null
    private var buttonStateCallback: SaveButtonStateCallBack? = null

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var minBid = 0
    private var maxBid = 0
    private var suggestBidPerClick = 0
    private var validation1 = true
    private var validation2 = true
    private var validation3 = true
    private lateinit var sharedViewModel: SharedViewModel
    private var currentBudget = 0
    private var queryListener: QueryListener? = null
    private var compositeSubscription: CompositeSubscription? = null
    private var productId: MutableList<String> = mutableListOf()
    private var groupId: Int? = 0
    private var priceDaily = 0


    companion object {
        private const val GROUP_NAME = "group_name"
        private const val PRICE_BID = "price_bid"
        private const val DAILY_BUDGET = "daily_budget"
        private const val GROUP_ID = "group_id"
        private const val NAME_EDIT = "isNameEdit"
        private const val MULTIPLIER = 40
        private const val GROUPKEY = "groupName"
        fun newInstance(bundle: Bundle?): EditGroupAdFragment {
            val fragment = EditGroupAdFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    init {
        initCompositeSubscriber()
    }

    override fun getScreenName(): String {
        return EditGroupAdFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditFormDefaultViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.activity_edit_form_ad), container, false)

    }

    private fun getLatestBid() {
        val dummyId: MutableList<Int> = mutableListOf()
        productId.forEach {
            dummyId.add(it.toInt())
        }
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions("product", dummyId))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onBidSuccessSuggestion, this::onBidErrorSuggestion)

    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        progressBar.visibility = View.GONE
        group_name.textFieldInput.setText(arguments?.getString(GROUPKEY))
        budget.textFieldInput.setText(data.priceBid!!.toString())
        priceDaily = data.priceDaily!!
        daily_budget.textFieldInput.setText(data.priceDaily.toString())
        if (priceDaily > suggestBidPerClick * MULTIPLIER) {
            radio2.isChecked = true
        }
    }

    private fun onError(it: Throwable) {}
    private fun onBidSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        suggestBidPerClick = data[0].suggestionBid
        minBid = data[0].minBid
        maxBid = data[0].maxBid
        viewModel.getGroupInfo(groupId.toString(), this::onSuccessGroupInfo, this::onError)

    }

    private fun getNewCompositeSubIfUnsubscribed(subscription: CompositeSubscription?): CompositeSubscription {
        return if (subscription == null || subscription.isUnsubscribed) {
            CompositeSubscription()
        } else subscription

    }

    private fun initCompositeSubscriber() {
        compositeSubscription = getNewCompositeSubIfUnsubscribed(compositeSubscription)
        compositeSubscription?.add(Observable.unsafeCreate(Observable.OnSubscribe<String> { subscriber ->
            queryListener = object : QueryListener {
                override fun onQueryChanged(query: String) {
                    subscriber.onNext(query)
                }
            }
        })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        Timber.d("text $e.localizedMessage")
                    }

                    override fun onNext(s: String?) {
                        if (s != null) {
                            Timber.d("Sending the text $s")
                            viewModel.validateGroup(s, this@EditGroupAdFragment::onSuccessGroupName, this@EditGroupAdFragment::onErrorGroupName)
                        }
                    }
                }))
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName.Data) {
        group_name.setError(false)
        validation1 = true
        actionEnable()
        group_name.setMessage("")
    }

    private fun onErrorGroupName(error: String) {
        if (group_name.textFieldInput.text.toString() != arguments?.getString(GROUPKEY)) {
            group_name.setError(true)
            validation1 = false
            actionEnable()
            group_name.setMessage(error)
            if (error == resources.getString(R.string.duplicate_group_name_error_wrong))
                group_name.setMessage(resources.getString(R.string.duplicate_group_name_error))
            else
                group_name.setMessage(error)
        }
    }

    private fun onBidErrorSuggestion(e: Throwable) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupId = arguments?.getString("groupId")!!.toInt()
        sharedViewModel.productId.observe(requireActivity(), Observer {
            productId = it
            getLatestBid()
        })
        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
        sharedViewModel.setGroupName(arguments?.getString(GROUPKEY)!!)
        sharedViewModel.setGroupId(arguments?.getString("groupId")!!.toInt())
        if (radio1.isChecked) {
            daily_budget.visibility = View.GONE

        }
        radio_group.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == radio1.id) {
                daily_budget.visibility = View.GONE

            } else {
                daily_budget.visibility = View.VISIBLE
            }
        }

        group_name.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (queryListener != null) {
                        queryListener?.onQueryChanged(s.toString())
                    }
                }
            }
        })

        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                currentBudget = number.toInt()
                val result = number.toInt()
                daily_budget.textFieldInput.setText((MULTIPLIER * result).toString())

                when {
                    result < minBid -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        validation2 = false
                        actionEnable()
                    }
                    result > maxBid -> {
                        validation2 = false
                        actionEnable()
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }
                    else -> {
                        validation2 = true
                        actionEnable()
                        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
                    }
                }
            }

        })
        daily_budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < MULTIPLIER * currentBudget) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.min_bid_error), MULTIPLIER * currentBudget))
                    validation3 = false
                    actionEnable()

                } else {
                    validation3 = true
                    daily_budget.setError(false)
                    daily_budget.setMessage("")
                    actionEnable()
                }
            }
        })
    }

    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(String.format(error, bid))
    }

    private fun actionEnable() {
        btnState = validation1 == true && validation2 == true && validation3 == true
        buttonStateCallback?.setButtonState()
    }

    override fun onAttach(context: Context) {
        if (context is EditFormAdActivityCallback) {
            callback = context

        }
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    fun getButtonState(): Boolean {
        return btnState
    }


    fun sendData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        try {
            dataMap[GROUP_NAME] = group_name.textFieldInput.text.toString()
            dataMap[PRICE_BID] = Integer.parseInt(budget.textFieldInput.text.toString().replace(",", ""))
            dataMap[DAILY_BUDGET] = Integer.parseInt(daily_budget.textFieldInput.text.toString().replace(",", ""))
            dataMap[GROUP_ID] = groupId
            dataMap[NAME_EDIT] = group_name.textFieldInput.text.toString() != arguments?.getString(GROUPKEY)
        } catch (e: NumberFormatException) {
        }
        return dataMap
    }


    override fun onDetach() {
        callback = null
        buttonStateCallback = null
        super.onDetach()
    }

    private interface QueryListener {
        fun onQueryChanged(query: String)
    }
}
