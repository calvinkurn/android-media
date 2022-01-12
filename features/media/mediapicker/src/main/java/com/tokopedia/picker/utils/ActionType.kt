package com.tokopedia.picker.utils

import com.tokopedia.picker.data.entity.Media

sealed class ActionType {
    class Add(val data: List<Media>, val error: String?): ActionType()
    class Remove(val data: List<Media>, val error: String?): ActionType()
    class Reorder(val data: List<Media>, val error: String?): ActionType()
}
