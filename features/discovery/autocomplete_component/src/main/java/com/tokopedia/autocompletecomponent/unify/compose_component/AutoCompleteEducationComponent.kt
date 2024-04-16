package com.tokopedia.autocompletecomponent.unify.compose_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.autocompletecomponent.unify.AutoCompleteUnifyDataView
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Preview
@Composable
private fun PreviewUi() {
    Surface {
        AutoCompleteEducationComponent(AutoCompleteUnifyDataView()) {}
    }
}

@Composable
internal fun AutoCompleteEducationComponent(
    item: AutoCompleteUnifyDataView,
    onItemClicked: (item: AutoCompleteUnifyDataView) -> Unit
) {
    val domainItem = item.domainModel

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth()
            .background(NestTheme.colors.NN._0)
            .border(1.dp, color = NestTheme.colors.NN._300, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            AutoCompleteLeftIconEducation(domainItem.image)
            NestTypography(
                text = domainItem.title.text,
                textStyle = NestTheme.typography.display2.copy(
                    color = NestTheme.colors.NN._950
                ),
                maxLines = 1,
                modifier = Modifier.padding(start = 4.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            AutocompleteRightLabelEducation(item = domainItem.label, onItemClicked = {
                onItemClicked(item)
            })
        }
    }
}
