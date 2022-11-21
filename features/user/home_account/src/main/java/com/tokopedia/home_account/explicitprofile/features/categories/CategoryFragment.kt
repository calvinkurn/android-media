package com.tokopedia.home_account.explicitprofile.features.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.databinding.FragmentExplicitProfileCategoryBinding
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_ID
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.EMPTY_PAGE_IMAGE_HEADER
import com.tokopedia.home_account.explicitprofile.data.ExplicitProfileGetQuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import com.tokopedia.home_account.explicitprofile.data.TemplateDataModel
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponentsBuilder
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileSharedViewModel
import com.tokopedia.home_account.explicitprofile.features.categories.sections.SectionAdapter
import com.tokopedia.home_account.explicitprofile.features.categories.sections.SectionViewHolder
import com.tokopedia.home_account.explicitprofile.features.popupinfo.SectionInfoBottomSheet
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalytics
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CategoryFragment: BaseDaggerFragment(), SectionViewHolder.SectionListener {

    @Inject
    lateinit var tracker: ExplicitProfileAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(key, CategoryViewModel::class.java) }
    private val viewModelShared by lazy { viewModelFragmentProvider.get(ExplicitProfileSharedViewModel::class.java) }

    private var viewBinding by autoClearedNullable<FragmentExplicitProfileCategoryBinding>()
    private val adapterQuestion = SectionAdapter(this)
    private var templeDataModel: TemplateDataModel? = TemplateDataModel()

    private var categoryId = 0
    private var templateName = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.application?.let {
            ExplicitProfileComponentsBuilder
                .getComponent(it)
                .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentExplicitProfileCategoryBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(CATEGORY_ID).orZero()
            templateName = it.getString(CATEGORY_TEMPLATE).orEmpty()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        showMainView(false)
        if (templateName.isNotEmpty()) {
            viewModel.getQuestion(templateName)
        }
    }

    private fun initObservers() {
        viewModel.questions.observe(viewLifecycleOwner) {
            when(it) {
                is ExplicitProfileResult.Loading -> {
                    showLoading(true)
                }
                is ExplicitProfileResult.Success -> {
                    showLoading(false)
                    onSuccessGetQuestions(it.data.explicitProfileQuestionDataModel)
                }
                is ExplicitProfileResult.Failure -> {
                    showLoading(false)
                    onError(it.error)
                }
            }
        }
    }

    private fun initViews() {
        if (categoryId == EMPTY_PAGE_ID && templateName.isEmpty()) {
            showEmptyPage()
            return
        }

        viewBinding?.questionsList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterQuestion
        }
    }

    private fun onSuccessGetQuestions(explicitProfileGetQuestionDataModel: ExplicitProfileGetQuestionDataModel) {
        explicitProfileGetQuestionDataModel.template.let {
            templeDataModel = it
            showMainView(it.sections.isNotEmpty())

            viewBinding?.apply {
                tickerCategory.setHtmlDescription(it.property.title)
            }

            viewModelShared.setDefaultTemplatesData(it)

            adapterQuestion.clearAllItems()
            adapterQuestion.setItems(it.sections)
            adapterQuestion.notifyDataSetChanged()
        }
    }

    private fun onError(error: MessageErrorException) {
        view?.let {
            showMainView(false)
            Toaster.build(it, ErrorHandler.getErrorMessage(context, error), Toaster.LENGTH_LONG).show()
        }
    }

    override fun onClickInfo(sectionsDataModel: SectionsDataModel) {
        tracker.clickOnInfoIcon()

        SectionInfoBottomSheet
            .createInstance(sectionsDataModel)
            .show(childFragmentManager)
    }

    override fun onQuestionSelected(questionDataModel: QuestionDataModel, isSelected: Boolean) {
        tracker.clickOnAnswers(questionDataModel.property.name)

        templeDataModel = templeDataModel?.apply {
            sections.forEach { section ->
                section.questions.find {
                    it.questionId == questionDataModel.questionId
                }?.property = questionDataModel.property
            }
        }

        templeDataModel?.let {
            viewModelShared.onAnswerChange(it)
        }
    }

    private fun showEmptyPage() {
        viewBinding?.emptyLayout?.apply {
            root.show()
            imageEmptyPage.loadImage(EMPTY_PAGE_IMAGE_HEADER) {
                useCache(true)
            }

            showMainView(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding?.apply {
            loading.visibility = if (isLoading) View.VISIBLE else View.GONE

            showMainView(!isLoading)
        }
    }

    private fun showMainView(isShow: Boolean) {
        viewBinding?.apply {
            categoryContentLayout.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.questions.removeObservers(this)
    }

    companion object {
        private var key = ""
        private const val CATEGORY_ID = "id"
        private const val CATEGORY_TEMPLATE = "template"

        fun createInstance(id: Int, templateName: String): Fragment {
            return CategoryFragment().apply {
                arguments = Bundle().apply {
                    key = id.toString()
                    putInt(CATEGORY_ID, id)
                    putString(CATEGORY_TEMPLATE, templateName)
                }
            }
        }
    }
}
