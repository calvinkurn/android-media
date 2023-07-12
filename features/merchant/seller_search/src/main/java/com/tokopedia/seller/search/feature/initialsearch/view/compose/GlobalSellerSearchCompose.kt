package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.components.NestSearchBar
import com.tokopedia.seller.search.R

@Composable
fun GlobalSellerSearchView(
    onBackBtnClicked: (String) -> Unit,
    onSearchBarKeywordSubmit: (String) -> Unit,
    onSearchbarCleared: () -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        content = {
            val (backBtn, searchBarUnify) = createRefs()

            var searchBarText by remember { mutableStateOf(String.EMPTY) }

            Image(
                modifier = Modifier
                    .constrainAs(backBtn) {
                        top.linkTo(searchBarUnify.top)
                        bottom.linkTo(searchBarUnify.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 12.dp)
                    .clickable { onBackBtnClicked(searchBarText) },
                painter = painterResource(id = R.drawable.ic_back_searchbar),
                contentDescription = null,
            )

            SearchBarUnify(
                modifier = Modifier.constrainAs(searchBarUnify) {
                        top.linkTo(parent.top)
                        start.linkTo(backBtn.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                onSearchBarKeywordSubmit = onSearchBarKeywordSubmit,
                onSearchbarCleared = onSearchbarCleared,
                onSearchBarTextChanged = {
                    searchBarText = it
                    onSearchBarTextChanged(it)
                }
            )
        }
    )
}

@Composable
private fun SearchBarUnify(
    modifier: Modifier,
    onSearchBarKeywordSubmit: (String) -> Unit,
    onSearchbarCleared: () -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {

    NestSearchBar(
        placeholderText = stringResource(id = R.string.placeholder_search_seller),
        modifier = modifier,
        onSearchBarCleared = onSearchbarCleared,
        onKeyboardSearchAction = onSearchBarKeywordSubmit,
        onTextChanged = {
            onSearchBarTextChanged(it)
        }
    )
}
