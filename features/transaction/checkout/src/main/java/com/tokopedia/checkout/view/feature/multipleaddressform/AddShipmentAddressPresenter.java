package com.tokopedia.checkout.view.feature.multipleaddressform;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

public class AddShipmentAddressPresenter implements IAddShipmentAddressPresenter {

    private RecipientAddressModel editableAddressModel;
    private MultipleAddressItemData multipleAddressItemData;
    private MultipleAddressAdapterData multipleAddressAdapterData;

    public AddShipmentAddressPresenter(RecipientAddressModel editableAddressModel) {
        this.editableAddressModel = editableAddressModel;
    }

    private void initiateEditableData() {
        RecipientAddressModel editableModel = new RecipientAddressModel();
        editableAddressModel = editableModel;
    }

    @Override
    public void initiateData(MultipleAddressAdapterData addressAdapterData, MultipleAddressItemData addressItemData) {
        multipleAddressAdapterData = addressAdapterData;
        multipleAddressItemData = addressItemData;
        initiateEditableData();
    }

    @Override
    public MultipleAddressItemData confirmAddData(MultipleAddressItemData baseItem, String quantity, String notes) {
        MultipleAddressItemData newItemData = new MultipleAddressItemData();
        newItemData.setProductQty(quantity);
        newItemData.setProductWeightFmt(multipleAddressItemData.getProductWeightFmt());
        newItemData.setCartPosition(multipleAddressItemData.getCartPosition());
        newItemData.setAddressPosition(multipleAddressAdapterData.getItemListData().size());
        newItemData.setParentId(multipleAddressItemData.getParentId());
        newItemData.setProductId(multipleAddressItemData.getProductId());
        newItemData.setProductNotes(notes);
        newItemData.setCartId("0");
        newItemData.setErrorCheckoutPriceLimit(baseItem.getErrorCheckoutPriceLimit());
        newItemData.setErrorFieldBetween(baseItem.getErrorFieldBetween());
        newItemData.setErrorFieldMaxChar(baseItem.getErrorFieldMaxChar());
        newItemData.setErrorFieldRequired(baseItem.getErrorFieldRequired());
        newItemData.setErrorProductAvailableStock(baseItem.getErrorProductAvailableStock());
        newItemData.setErrorProductAvailableStockDetail(baseItem.getErrorProductAvailableStockDetail());
        newItemData.setErrorProductMaxQuantity(baseItem.getErrorProductMaxQuantity());
        newItemData.setErrorProductMinQuantity(baseItem.getErrorProductMinQuantity());
        newItemData.setMaxQuantity(baseItem.getMaxQuantity());
        newItemData.setMinQuantity(baseItem.getMinQuantity());
        newItemData.setMaxRemark(baseItem.getMaxRemark());
        return newItemData;
    }

    @Override
    public MultipleAddressItemData confirmEditData(String quantity, String notes) {
        multipleAddressItemData.setProductQty(quantity);
        multipleAddressItemData.setRecipientAddressModel(editableAddressModel);
        multipleAddressItemData.setProductNotes(notes);
        return multipleAddressItemData;
    }

    @Override
    public RecipientAddressModel getEditableModel() {
        return editableAddressModel;
    }

    @Override
    public void setEditableModel(RecipientAddressModel newEditableModel) {
        this.editableAddressModel = newEditableModel;

    }

    @Override
    public MultipleAddressAdapterData getMultipleAddressAdapterData() {
        return multipleAddressAdapterData;
    }

    @Override
    public MultipleAddressItemData getMultipleItemData() {
        return multipleAddressItemData;
    }
}
