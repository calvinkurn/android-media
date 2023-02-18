package com.tokopedia.topchat.chattemplate.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.view.activity.EditTemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingTypeFactoryImpl
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder
import com.tokopedia.topchat.chattemplate.view.dialog.TemplateInfoBottomSheet
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.ChatTemplateViewModel
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.util.SimpleItemTouchHelperCallback
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.build
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

open class TemplateChatFragment : BaseDaggerFragment(), TemplateChatContract.View {
    private var switchTemplate: SwitchUnify? = null
    private var recyclerView: RecyclerView? = null
    private var templateContainer: View? = null
    private var info: View? = null
    private var loading: View? = null
    private var content: View? = null
    private var typeFactory = TemplateChatSettingTypeFactoryImpl(this)
    private var layoutManager: LinearLayoutManager? = null
    override var adapter = TemplateChatSettingAdapter(typeFactory, this)
    override var list: ArrayList<String> = adapter.getListString()

    @Inject
    lateinit var viewModel: ChatTemplateViewModel

    @JvmField
    @Inject
    var analytic: ChatTemplateAnalytics? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var templateInfo = TemplateInfoBottomSheet()
    private var isSeller = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments()
    }

    private fun initArguments() {
        arguments?.let {
            isSeller = it.getBoolean(TemplateChatActivity.PARAM_IS_SELLER)
        }
        if (GlobalConfig.isSellerApp()) {
            isSeller = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_template_chat,
            container,
            false
        ).also {
            bindView(it)
            getTemplate()
        }
    }

    private fun bindView(view: View) {
        loading = view.findViewById(R.id.loading_search)
        content = view.findViewById(R.id.content)
        recyclerView = view.findViewById(R.id.recycler_view)
        info = view.findViewById(R.id.template_list_info)
        switchTemplate = view.findViewById(R.id.switch_chat_template)
        templateContainer = view.findViewById(R.id.template_container)
    }

    private fun getTemplate() {
        showLoading()
        viewModel.getTemplate(isSeller)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        setupRecyclerView()
        setupSwitchTemplate()
        setupInfo()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()
        layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.layoutManager = layoutManager
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(recyclerView)
    }

    private fun setupSwitchTemplate() {
        switchTemplate?.setOnClickListener {
            val b = switchTemplate?.isChecked ?: false
            analytic?.trackOnCheckedChange(b)
            switchTemplateAvailability(b)
            if (b) {
                templateContainer?.visibility = View.VISIBLE
            } else {
                templateContainer?.visibility = View.GONE
            }
        }
    }

    private fun switchTemplateAvailability(isEnabled: Boolean) {
        showLoading()
        viewModel.toggleTemplate(isSeller, isEnabled)
    }

    private fun setupInfo() {
        info?.setOnClickListener {
            templateInfo.show(childFragmentManager, TemplateInfoBottomSheet::class.simpleName)
        }
    }

    override fun getScreenName(): String {
        return "TemplateChat"
    }

    override fun initInjector() {
        initializeComponent().inject(this)
    }

    private fun initializeComponent(): TemplateChatComponent {
        return ActivityComponentFactory.instance.createActivityComponent(requireActivity())
    }

    override fun setTemplate(listTemplate: List<Visitable<*>>?) {
        adapter.setList(listTemplate)
        prepareResult()
    }

    override fun onDrag(viewHolder: ItemTemplateChatViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder)
    }

    override fun onEnter(message: String?, adapterPosition: Int) {
        if (message == null && adapter.getList().size > 5) {
            showUnifyToaster(
                context?.getString(R.string.limited_template_chat_warning) ?: "",
                TYPE_NORMAL
            )
        } else {
            val intent = EditTemplateChatActivity.createInstance(activity)
            val bundle = Bundle()
            bundle.putString(InboxMessageConstant.PARAM_MESSAGE, message)
            bundle.putInt(InboxMessageConstant.PARAM_POSITION, adapterPosition)
            bundle.putInt(InboxMessageConstant.PARAM_NAV, adapter.getList().size - 1)
            bundle.putStringArrayList(InboxMessageConstant.PARAM_ALL, adapter.getListString())
            if (message == null) {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, CREATE)
                analytic?.trackAddTemplateChat()
            } else {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, EDIT)
                analytic?.trackEditTemplateChat()
            }
            bundle.putBoolean(TemplateChatActivity.PARAM_IS_SELLER, isSeller)
            intent.putExtras(bundle)
            startActivityForResult(intent, 100)
            activity?.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out)
        }
    }

    override fun setChecked(enable: Boolean) {
        switchTemplate?.isChecked = enable
        if (enable) {
            templateContainer?.visibility = View.VISIBLE
        } else {
            templateContainer?.visibility = View.GONE
        }
    }

    override fun reArrange(from: Int, to: Int) {
        switchTemplate?.let {
            showLoading()
            viewModel.rearrangeTemplate(isSeller, from, to)
        }
    }

    override fun revertArrange(from: Int, to: Int) {
        adapter.revertArrange(to, from)
    }

    private fun arrangeList(from: Int, to: Int): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (i in 0 until adapter.getList().size - 1) {
            arrayList.add(i + 1)
        }
        arrayList.remove(Integer.valueOf(from + 1))
        arrayList.add(to, from + 1)
        return arrayList
    }

    override fun successSwitch() {
        prepareResultSwitch()
    }

    override fun showLoading() {
        content?.visibility = View.GONE
        loading?.visibility = View.VISIBLE
    }

    override fun finishLoading() {
        content?.visibility = View.VISIBLE
        loading?.visibility = View.GONE
    }

    override fun showError(errorMessage: Throwable) {
        showUnifyToaster(ErrorHandler.getErrorMessage(context, errorMessage), TYPE_ERROR)
    }

    override fun successRearrange() {
        val text = context?.getString(R.string.success_rearrange_template_chat) ?: ""
        showUnifyToaster(text, TYPE_NORMAL)
        prepareResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> if (resultCode == Activity.RESULT_OK) {
                getTemplate()
                val string = data?.getStringExtra(LIST_RESULT)
                val index = data?.getIntExtra(INDEX_RESULT, -1) ?: -1
                var text = ""
                when (data?.getIntExtra(MODE_RESULT, 0)) {
                    CREATE -> {
                        adapter.add(string)
                        text = activity?.getString(R.string.success_add_template_chat) ?: ""
                    }
                    EDIT -> {
                        adapter.edit(index, string)
                        text = context?.getString(R.string.success_edit_template_chat) ?: ""
                    }
                    DELETE -> {
                        adapter.delete(index)
                        text = context?.getString(R.string.success_delete_template_chat) ?: ""
                    }
                    else -> {
                    }
                }
                prepareResult()
                showUnifyToaster(text, TYPE_NORMAL)
            }
            else -> {
            }
        }
    }

    private fun prepareResultSwitch() {
        if (switchTemplate?.isChecked == true) {
            prepareResult()
        } else {
            val intent = Intent()
            intent.putStringArrayListExtra(LIST_RESULT, ArrayList())
            activity?.setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun prepareResult() {
        val intent = Intent()
        intent.putStringArrayListExtra(LIST_RESULT, adapter.getListString())
        activity?.setResult(Activity.RESULT_OK, intent)
    }

    private fun showUnifyToaster(text: String, type: Int) {
        view?.let {
            build(it, text, LENGTH_SHORT, type).show()
        }
    }

    private fun setupObservers() {
        viewModel.chatTemplate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val temp: ArrayList<Visitable<*>> = it.data.listTemplate
                    val size: Int = temp.size
                    temp.add(
                        TemplateChatUiModel(
                            false,
                            size
                        )
                    )
                    setTemplate(temp)
                    setChecked(it.data.isEnabled)
                }
                is Fail -> setTemplate(null)
            }
            finishLoading()
        }

        viewModel.templateAvailability.observe(viewLifecycleOwner) {
            val isEnabled = it.first
            when (val result = it.second) {
                is Success -> {
                    if (result.data == Int.ONE) {
                        successSwitch()
                    }
                }
                is Fail -> {
                    showError(result.throwable)
                    setChecked(!isEnabled)
                }
            }
            finishLoading()
        }

        viewModel.arrangeTemplate.observe(viewLifecycleOwner) {
            if (it.error != null) {
                it.error?.let { error ->
                    showError(error)
                }
                revertArrange(it.from, it.to)
            } else {
                successRearrange()
            }
            finishLoading()
        }
    }

    companion object {
        const val CREATE = 0
        const val EDIT = 1
        const val DELETE = -1
        const val LIST_RESULT = "string"
        const val INDEX_RESULT = "index"
        const val MODE_RESULT = "mode"

        fun createInstance(extras: Bundle?): TemplateChatFragment {
            val fragment = TemplateChatFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
