package com.tokopedia.weaver

abstract class WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR, DEF_VAL>(var keyData:KEY_TYPE, var accessHelper:ACS_HLPR, var defaultValue:DEF_VAL){
    abstract fun checkCondition():Boolean
}
