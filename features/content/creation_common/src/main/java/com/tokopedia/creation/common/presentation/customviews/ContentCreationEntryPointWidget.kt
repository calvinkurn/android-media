package com.tokopedia.creation.common.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.creation.common.di.ContentCreationComponent
import com.tokopedia.creation.common.di.DaggerContentCreationComponent
import com.tokopedia.creation.common.presentation.bottomsheet.ContentCreationBottomSheet
import com.tokopedia.creation.common.presentation.viewmodel.ContentCreationViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.creation.common.R as creationcommonR

/**
 * Created By : Muhammad Furqan on 04/10/23
 */
class ContentCreationEntryPointWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    var onClickListener: () -> Unit = {}

    private val component = createComponent()
    private val factory: ViewModelProvider.Factory = component.contentCreationFactory()

    private var viewModel: ContentCreationViewModel? = null
    var creationBottomSheetListener: ContentCreationBottomSheet.ContentCreationBottomSheetListener? =
        null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        viewModel = ViewModelProvider(
            owner = findViewTreeViewModelStoreOwner()!!,
            factory = factory
        )[ContentCreationViewModel::class.java]

        getFragmentManager()?.addFragmentOnAttachListener { _, childFragment ->
            when (childFragment) {
                is ContentCreationBottomSheet -> {
                    childFragment.shouldShowPerformanceAction = true
                    creationBottomSheetListener?.let {
                        childFragment.listener = it
                    }
                }
            }
        }
    }

    @Composable
    override fun Content() {
        val creationConfig = viewModel?.creationConfig?.collectAsStateWithLifecycle()?.value

        if (creationConfig is Success && creationConfig.data.creationItems.isNotEmpty()) {
            ContentCreationEntryPointComponent(
                iconId = IconUnify.VIDEO,
                text = MethodChecker.fromHtml(stringResource(id = creationcommonR.string.content_creation_entry_point_desription))
                    .toAnnotatedString(),
                buttonText = stringResource(id = creationcommonR.string.content_creation_entry_point_button_label)
            ) {
                onClickListener()

                getFragmentManager()?.let { fm ->
                    ContentCreationBottomSheet
                        .getFragment(fm, context.classLoader)
                        .show(
                            fm,
                            creationConfig = creationConfig.data
                        )
                }
            }
        }
    }

    fun fetchConfig() {
        viewModel?.fetchConfig()
    }

    private fun createComponent(): ContentCreationComponent =
        DaggerContentCreationComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()

    private fun getFragmentManager(): FragmentManager? =
        try {
            val fragment = findFragment<Fragment>()
            fragment.childFragmentManager
        } catch (_: IllegalStateException) {
            when (context) {
                is AppCompatActivity -> {
                    (context as AppCompatActivity).supportFragmentManager
                }

                is FragmentActivity -> {
                    (context as FragmentActivity).supportFragmentManager
                }

                else -> null
            }
        } catch (_: Throwable) {
            null
        }
}

@Composable
fun ContentCreationEntryPointComponent(
    iconId: Int,
    text: String,
    buttonText: String,
    onClick: () -> Unit
) {
    ContentCreationEntryPointComponent(
        iconId = iconId,
        text = AnnotatedString(text),
        buttonText = buttonText,
        onClick = onClick
    )
}

@Composable
fun ContentCreationEntryPointComponent(
    iconId: Int,
    text: AnnotatedString,
    buttonText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 2.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(NestTheme.colors.NN._0)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestIcon(iconId = iconId, modifier = Modifier.size(24.dp))
        NestTypography(
            text = text,
            textStyle = NestTheme.typography.display3,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp)
                .fillMaxWidth()
                .weight(1f)
        )
        NestButton(
            text = buttonText,
            onClick = onClick,
            variant = ButtonVariant.FILLED,
            size = ButtonSize.SMALL
        )
    }
}

@Preview
@Composable
fun ContentCreationEntryPointComponentPreview() {
    ContentCreationEntryPointComponent(
        iconId = IconUnify.VIDEO,
        text = "Promosikan produkmu dengan Live, Video, Foto & Story",
        buttonText = "Buat Konten"
    ) {
    }
}
