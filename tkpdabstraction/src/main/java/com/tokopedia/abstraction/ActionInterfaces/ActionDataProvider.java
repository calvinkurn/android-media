package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionDataProvider<OUTPUT_DATA,INPUT_DATA> {
    OUTPUT_DATA getData(int actionId, INPUT_DATA dataObject);
}
