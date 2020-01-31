package com.tokopedia.weaver

class Weaver{
    companion object {
        fun <KEY_TYPE, ACS_HLPR> executeWeave(weaverInterface: WeaveInterface, weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR>, weaveAsyncProvider: WeaveAsyncProvider) {
            if (weaverConditionCheckProvider.checkCondition()) {
                weaveAsyncProvider.executeAsync(weaverInterface)
            }else{
                weaverInterface.execute()
            }
        }

        fun <KEY_TYPE, ACS_HLPR> executeWeaveCoRoutine(weaverInterface: WeaveInterface, weaverConditionCheckProvider: WeaverConditionCheckProvider<KEY_TYPE, ACS_HLPR>) {
            val weaveAsyncProvider: WeaveAsyncProvider = CoRoutineAsyncWeave()
            executeWeave(weaverInterface, weaverConditionCheckProvider, weaveAsyncProvider)
        }
    }
}