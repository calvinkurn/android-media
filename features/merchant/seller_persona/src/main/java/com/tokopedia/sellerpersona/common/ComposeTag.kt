package com.tokopedia.sellerpersona.common

/**
 * Created by @ilhamsuaib on 26/10/23.
 */

object ComposeTag {

    object Opening {
        const val ILLUSTRATION = "imgSpOpening"
        const val TITLE = "tvSpOpeningTitle"
        const val SUB_TITLE = "tvSpOpeningSubTitle"
        const val INFO = "tvSpOpeningFooter"
        const val BTN_START = "btnOpeningStartQuiz"
        const val BTN_LATER = "btnOpeningTryLater"
    }

    object Result {
        const val RESULT_INFO_LIST = "rvSpResultInfoList"
        const val LBL_STATUS = "tvSpLblActivatePersonaStatus"
        const val BTN_APPLY = "btnSpApplyPersona"
        const val BTN_QUIZ = "btnSpRetryQuestionnaire"
        const val BTN_SELECT_TYPE = "tvSpSelectManualType"
        const val INFO_ITEM = "tvSpResultInfoItem"
        const val OWNER_INFO = "tvSpLblOwnerInfo"
        const val IMG_BACKDROP = "imgSpResultBackdrop"
        const val IMG_AVATAR = "imgSpResultAvatar"
        const val LABEL_TYPE = "tvSpLblSellerType"
        const val SELLER_TYPE = "tvSpSellerType"
        const val TYPE_NOTE = "tvSpSellerTypeNote"
    }

    object Questionnaire {
        const val PROGRESS = "progressBarPersonaQuestionnaire"
        const val BTN_PREV = "btnSpPrev"
        const val BTN_NEXT = "btnSpNext"
        const val QUESTION_TITLE = "tvSpQuestionTitle"
        const val QUESTION_SUB_TITLE = "tvSpQuestionSubtitle"
        const val CB_OPTION = "cbSpMultipleOption"
        const val OPTION_TITLE = "tvSpOptionTitle"
    }

    object SelectType {
        const val BTN_SELECT_TYPE = "btnSpSelectType"
        const val CONTAINER = "containerSpItemPersonaType"
        const val RAD_PERSONA = "radioSpPersonaType"
        const val PERSONA_TYPE = "tvSpPersonaType"
    }
}