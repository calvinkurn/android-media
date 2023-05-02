package com.tokopedia.usercomponents.userconsent.ui

sealed class ConsentType{
    class SingleInfo: ConsentType()
    class SingleChecklist: ConsentType()
    class MultipleChecklist: ConsentType()
}
