package com.tokopedia.checkout.subfeature.multiple_address.view;

import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.checkout.subfeature.multiple_address.domain.model.MultipleAddressAdapterData;
import com.tokopedia.checkout.subfeature.multiple_address.domain.model.MultipleAddressItemData;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

public interface IAddShipmentAddressPresenter {

    void initiateData(MultipleAddressAdapterData addressAdapterData,
                      MultipleAddressItemData addressItemData);

    MultipleAddressItemData confirmAddData(MultipleAddressItemData baseItem, String quantity, String notes);

    MultipleAddressItemData confirmEditData(String quantity, String notes);

    RecipientAddressModel getEditableModel();

    void setEditableModel(RecipientAddressModel newEditableModel);

    MultipleAddressAdapterData getMultipleAddressAdapterData();

    MultipleAddressItemData getMultipleItemData();

}
