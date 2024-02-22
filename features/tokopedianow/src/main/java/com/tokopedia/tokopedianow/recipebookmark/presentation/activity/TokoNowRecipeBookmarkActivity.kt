package com.tokopedia.tokopedianow.recipebookmark.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics
import com.tokopedia.tokopedianow.recipebookmark.di.component.DaggerRecipeBookmarkComponent
import com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel.TokoNowRecipeBookmarkViewModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.layout.RecipeBookmarkLayout
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkAction
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

class TokoNowRecipeBookmarkActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TokoNowRecipeBookmarkViewModel

    @Inject
    lateinit var analytics: RecipeBookmarkAnalytics

    @Inject
    lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        if(remoteConfig.getBoolean(RemoteConfigKey.ANDROID_DISABLE_RECIPE_BOOKMARK_COMPOSE, false)) {
            startActivity(
                Intent(
                    this,
                    com.tokopedia.tokopedianow.oldrecipebookmark.presentation.activity.TokoNowRecipeBookmarkActivity::class.java
                )
            )
            finish()
        }
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {
                val state = viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel.uiAction) {
                    viewModel.uiAction.collect {
                        when (it) {
                            is RecipeBookmarkAction.PressBackButton -> finish()
                            is RecipeBookmarkAction.ShowToaster -> showToaster(it)
                            is RecipeBookmarkAction.UnregisteredAction -> {
                                /* do nothing */
                            }
                        }
                    }
                }

                RecipeBookmarkLayout(state.value, analytics, viewModel::onEvent)
            }
        }
        loadBookmarkList()
    }

    private fun loadBookmarkList() {
        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)
    }

    private fun injectDependencies() {
        DaggerRecipeBookmarkComponent.builder()
            .baseAppComponent((applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun showToaster(data: RecipeBookmarkAction.ShowToaster) {
        if (data.model?.isSuccess == true) {
            showSuccessToaster(data)
        } else {
            showErrorToaster(data)
        }
    }

    private fun showSuccessToaster(data: RecipeBookmarkAction.ShowToaster?) {
        if (data?.model == null) return

        val model = data.model
        val recipeId = model.recipeId
        val position = data.position.orZero()
        val isRemoving = false

        val text = getString(
            R.string.tokopedianow_recipe_toaster_description_success_removing_bookmark,
            title
        )
        val actionText = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_cancel)

        Toaster.build(
            view = findViewById(android.R.id.content),
            text = text,
            type = Toaster.TYPE_NORMAL,
            actionText = actionText,
            clickListener = {
                viewModel.onEvent(
                    RecipeBookmarkEvent.AddRecipeBookmark(
                        recipeId = recipeId,
                        position = position,
                        isRemoving = isRemoving
                    )
                )
                analytics.clickCancelUnBookmarkToaster()
            }
        ).show()
        analytics.impressUnBookmarkToaster()
    }

    private fun showErrorToaster(data: RecipeBookmarkAction.ShowToaster?) {
        if (data?.model == null) return

        val model = data.model
        val title = model.title
        val recipeId = model.recipeId
        val message = model.message
        val throwable = model.throwable
        val position = data.position.orZero()

        val text = if (throwable == null) {
            message
        } else {
            getString(
                if (data.isRemoving) {
                    R.string.tokopedianow_recipe_toaster_description_failed_removing_bookmark
                } else {
                    R.string.tokopedianow_recipe_toaster_description_failed_adding_bookmark
                }
            )
        }
        val actionText = getString(R.string.tokopedianow_recipe_bookmark_toaster_cta_try_again)

        Toaster.build(
            view = findViewById(android.R.id.content),
            text = text,
            type = Toaster.TYPE_ERROR,
            actionText = actionText,
            clickListener = {
                if (data.isRemoving) {
                    removeRecipeBookmark(position, title, recipeId)
                } else {
                    addRecipeBookmark(position, recipeId)
                }
            }
        ).show()
    }

    private fun addRecipeBookmark(position: Int, recipeId: String) {
        viewModel.onEvent(
            RecipeBookmarkEvent.AddRecipeBookmark(
                recipeId = recipeId,
                position = position,
                isRemoving = false
            )
        )
    }

    private fun removeRecipeBookmark(position: Int, title: String, recipeId: String) {
        viewModel.onEvent(
            RecipeBookmarkEvent.RemoveRecipeBookmark(
                title = title,
                position = position,
                recipeId = recipeId,
                isRemoving = true
            )
        )
    }
}
