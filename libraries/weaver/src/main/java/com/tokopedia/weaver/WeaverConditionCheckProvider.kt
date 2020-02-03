package com.tokopedia.weaver

abstract class WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR>(var keyData:KEY_TYPE, var accessHelper:ACS_HLPR){
    abstract fun checkCondition():Boolean
}
