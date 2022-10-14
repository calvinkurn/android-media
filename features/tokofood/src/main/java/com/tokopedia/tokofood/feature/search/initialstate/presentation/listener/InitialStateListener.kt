package com.tokopedia.tokofood.feature.search.initialstate.presentation.listener

import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.ChipsItemAdapter
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.CuisineItemViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.HeaderRecentSearchViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.RecentSearchItemViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.SeeMoreCuisineViewHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder.SeeMoreRecentSearchViewHolder

interface InitialStateListener :
    ChipsItemAdapter.ChipsItemListener,
    CuisineItemViewHolder.ActionListener,
    HeaderRecentSearchViewHolder.ActionListener,
    RecentSearchItemViewHolder.ActionListener,
    SeeMoreCuisineViewHolder.ActionListener,
    SeeMoreRecentSearchViewHolder.ActionListener