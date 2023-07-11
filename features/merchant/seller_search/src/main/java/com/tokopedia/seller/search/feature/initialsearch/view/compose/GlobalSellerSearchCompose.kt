package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun GlobalSellerSearchCompose() {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        content = {
            val (backBtn, searchBarUnify) = createRefs()

//            AppCompatImageView(
//                modifier = Modifier
//                    .constrainAs(backBtn) {
//                        top.linkTo(searchBarUnify.top)
//                        bottom.linkTo(searchBarUnify.bottom)
//                        start.linkTo(parent.start)
//                    }
//                    .padding(start = 12.dp)
//                    .clickable { /* todo handle click when back */ },
//
//
//            )
        }
    )
}
