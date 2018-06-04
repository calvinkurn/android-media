package com.tokopedia.checkout.view.view.multipleaddressform;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

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
        editableModel.setId(multipleAddressItemData.getAddressId());
        editableModel.setRecipientName(multipleAddressItemData.getAddressReceiverName());
        editableModel.setAddressName(multipleAddressItemData.getAddressTitle());
        editableModel.setAddressCityName(multipleAddressItemData.getAddressCityName());
        editableModel.setAddressCountryName(multipleAddressItemData.getAddressCountryName());
        editableModel.setAddressPostalCode(multipleAddressItemData.getAddressPostalCode());
        editableModel.setAddressProvinceName(multipleAddressItemData.getAddressProvinceName());
        editableModel.setAddressStreet(multipleAddressItemData.getAddressStreet());
        editableModel.setRecipientPhoneNumber(multipleAddressItemData.getRecipientPhoneNumber());
        editableModel.setDestinationDistrictId(multipleAddressItemData.getDestinationDistrictId());
        editableModel.setDestinationDistrictName(multipleAddressItemData.getDestinationDistrictName());
        editableModel.setCityId(multipleAddressItemData.getCityId());
        editableModel.setProvinceId(multipleAddressItemData.getProvinceId());
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
        newItemData.setAddressId(editableAddressModel.getId());
        newItemData.setParentId(multipleAddressItemData.getParentId());
        newItemData.setProductId(multipleAddressItemData.getProductId());
        newItemData.setAddressTitle(editableAddressModel.getAddressName());
        newItemData.setAddressReceiverName(editableAddressModel.getRecipientName());
        newItemData.setAddressStreet(editableAddressModel.getAddressStreet());
        newItemData.setAddressCityName(editableAddressModel.getAddressCityName());
        newItemData.setAddressProvinceName(editableAddressModel.getAddressProvinceName());
        newItemData.setAddressCountryName(editableAddressModel.getAddressCountryName());
        newItemData.setAddressPostalCode(editableAddressModel.getAddressPostalCode());
        newItemData.setRecipientPhoneNumber(editableAddressModel.getRecipientPhoneNumber());
        newItemData.setCityId(editableAddressModel.getCityId());
        newItemData.setProvinceId(editableAddressModel.getProvinceId());
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
        multipleAddressItemData.setAddressId(editableAddressModel.getId());
        multipleAddressItemData.setAddressTitle(editableAddressModel.getAddressName());
        multipleAddressItemData.setAddressReceiverName(editableAddressModel.getRecipientName());
        multipleAddressItemData.setAddressStreet(editableAddressModel.getAddressStreet());
        multipleAddressItemData.setAddressCityName(editableAddressModel.getAddressCityName());
        multipleAddressItemData.setAddressProvinceName(editableAddressModel.getAddressProvinceName());
        multipleAddressItemData.setAddressCountryName(editableAddressModel.getAddressCountryName());
        multipleAddressItemData.setAddressPostalCode(editableAddressModel.getAddressPostalCode());
        multipleAddressItemData.setRecipientPhoneNumber(editableAddressModel.getRecipientPhoneNumber());
        multipleAddressItemData.setCityId(editableAddressModel.getCityId());
        multipleAddressItemData.setProvinceId(editableAddressModel.getProvinceId());
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
