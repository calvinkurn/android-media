package com.tokopedia.abstraction.Actions.interfaces;

import java.io.Serializable;

public interface ActionDataProvider<OUTPUT_DATA,INPUT_DATA> extends Serializable {
    OUTPUT_DATA getData(int actionId, INPUT_DATA dataObject);
}
