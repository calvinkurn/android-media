package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.getAsSerializable
import com.tokopedia.people.viewmodels.FollowListViewModel
import com.tokopedia.people.views.screen.FollowListErrorScreen
import com.tokopedia.people.views.screen.FollowListScreen
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.appLink
import com.tokopedia.people.views.uimodel.state.FollowListState
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

@SuppressLint("UnsafeFragmentConstructor")
class FollowListFragment @Inject internal constructor(
    viewModelFactory: FollowListViewModel.Factory
) : Fragment() {

    private val viewModel by viewModels<FollowListViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val type = getArgsType() ?: error("Follow List Type must be set")
                val profileIdentifier = getArgsProfileIdentifier()
                    ?: error("Profile Identifier must be set")
                return viewModelFactory.create(type, profileIdentifier) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            val onPeopleClicked: (PeopleUiModel) -> Unit = {
                RouteManager.route(context, it.appLink)
            }

            val onFollowClicked: (PeopleUiModel) -> Unit = {
                viewModel.onAction(FollowListAction.Follow(it))
            }

            val onLoadMore: () -> Unit = {
                viewModel.onAction(FollowListAction.LoadMore)
            }

            val onRefresh: () -> Unit = {
                viewModel.onAction(FollowListAction.Refresh)
            }

            setContent {
                val state: FollowListState by viewModel.uiState.collectAsStateWithLifecycle()

                if (state.result?.isFailure == true && state.followList.isEmpty()) {
                    Box(Modifier.fillMaxSize()) {
                        FollowListErrorScreen(
                            isLoading = state.isLoading,
                            onRefreshButtonClicked = onRefresh
                        )
                    }
                } else {
                    FollowListScreen(
                        people = state.followList.toPersistentList(),
                        hasNextPage = state.hasNextPage,
                        onLoadMore = onLoadMore,
                        onPeopleClicked = onPeopleClicked,
                        onFollowClicked = onFollowClicked,
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(FollowListAction.Init)
    }

    private fun setArgsType(type: FollowListType) {
        getOrCreateArgs().putSerializable(ARGS_FOLLOW_LIST_TYPE, type)
    }

    private fun setArgsProfileIdentifier(identifier: String) {
        getOrCreateArgs().putString(ARGS_PROFILE_IDENTIFIER, identifier)
    }

    private fun getArgsType(): FollowListType? {
        return requireArguments().getAsSerializable<FollowListType>(ARGS_FOLLOW_LIST_TYPE)
    }

    private fun getArgsProfileIdentifier(): String? {
        return requireArguments().getString(ARGS_PROFILE_IDENTIFIER)
    }

    private fun getOrCreateArgs(): Bundle {
        return arguments ?: Bundle().also {
            arguments = it
        }
    }

    companion object {

        private const val ARGS_FOLLOW_LIST_TYPE = "args_follow_list_type"
        private const val ARGS_PROFILE_IDENTIFIER = "args_profile_identifier"
        fun get(
            fragmentManager: FragmentManager,
            tag: String = "FollowListFragment"
        ): FollowListFragment? {
            return fragmentManager.findFragmentByTag(tag) as? FollowListFragment
        }

        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            param: Param
        ): FollowListFragment {
            val fragment = fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FollowListFragment::class.java.name
            ) as FollowListFragment

            fragment.setArgsType(param.type)
            fragment.setArgsProfileIdentifier(param.profileId)

            return fragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            param: Param
        ): FollowListFragment {
            return get(fragmentManager) ?: create(fragmentManager, classLoader, param)
        }
    }

    data class Param(
        val type: FollowListType,
        val profileId: String
    )
}
