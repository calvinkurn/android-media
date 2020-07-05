package com.tokopedia.shop.open.listener

interface SurveyListener {

    fun onCheckedCheckbox(questionId: Int, choiceId: Int)
    fun onUncheckedCheckbox(questionId: Int, choiceId: Int)

}