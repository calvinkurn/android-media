package com.tokopedia.shop.open.shop_open_revamp.listener

interface SurveyListener {

    fun onCheckedCheckbox(questionId: Int, choiceId: Int)
    fun onUncheckedCheckbox(questionId: Int, choiceId: Int)

}