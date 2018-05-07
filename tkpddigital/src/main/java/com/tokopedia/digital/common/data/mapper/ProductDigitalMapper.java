package com.tokopedia.digital.common.data.mapper;

import com.tokopedia.digital.common.data.entity.response.Field;
import com.tokopedia.digital.common.data.entity.response.OperatorBannerEntity;
import com.tokopedia.digital.common.data.entity.response.RechargeCategoryDetail;
import com.tokopedia.digital.common.data.entity.response.Validation;
import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.ClientNumber;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.model.Promo;
import com.tokopedia.digital.product.view.model.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalMapper {

    public CategoryData transformCategoryData(RechargeCategoryDetail entity) throws MapperDataException {
        CategoryData categoryData = new CategoryData();
        categoryData.setTitleText(entity.getTitle());
        categoryData.setCategoryId(entity.getId());
        categoryData.setDefaultOperatorId(entity.getDefaultOperatorId());
        categoryData.setIcon(entity.getIcon());
        categoryData.setIconUrl(entity.getIconUrl());
        categoryData.setName(entity.getName());
        categoryData.setInstantCheckout(entity.getInstantCheckout());
        categoryData.setNew(entity.getIsNew());
        categoryData.setSlug(entity.getSlug());
        categoryData.setOperatorStyle(entity.getOperatorStyle());
        categoryData.setOperatorLabel(entity.getOperatorLabel());

        categoryData.setClientNumberList(transformClientNumberList(entity));
        categoryData.setOperatorList(transformOperators(entity));
        categoryData.setBannerDataListIncluded(transformBanners(entity.getBanners()));
        categoryData.setOtherBannerDataListIncluded(transformBanners(entity.getOtherBanners()));

        return categoryData;
    }

    /**
     * Helper function to transfor client number list
     *
     * @param entity
     * @return
     */
    private List<ClientNumber> transformClientNumberList(RechargeCategoryDetail entity) {
        List<ClientNumber> clientNumberCategoryList = new ArrayList<>();

        if (entity != null && entity.getClientNumber() != null && entity.getClientNumber().getName().equalsIgnoreCase(ClientNumber.DEFAULT_TYPE_CONTRACT)) {
            ClientNumber clientNumberCategory = new ClientNumber();
            clientNumberCategory.setName(entity.getClientNumber().getName());
            clientNumberCategory.set_default(entity.getClientNumber().getDefault());
            clientNumberCategory.setType(entity.getClientNumber().getType());
            clientNumberCategory.setPlaceholder(entity.getClientNumber().getPlaceholder());
            clientNumberCategory.setText(entity.getClientNumber().getText());
            List<com.tokopedia.digital.product.view.model.Validation> validationCategoryList
                    = new ArrayList<>();
            for (Validation validation
                    : entity.getClientNumber().getValidation()) {
                com.tokopedia.digital.product.view.model.Validation validationCategory =
                        new com.tokopedia.digital.product.view.model.Validation();
                validationCategory.setError(validation.getError());
                validationCategory.setRegex(validation.getRegex());
                validationCategoryList.add(validationCategory);
            }
            clientNumberCategory.setValidation(validationCategoryList);
            clientNumberCategoryList.add(clientNumberCategory);
        }

        return clientNumberCategoryList;
    }

    /**
     * Helper function to transform operator list
     *
     * @param entity
     * @return
     */
    private List<Operator> transformOperators(RechargeCategoryDetail entity) {
        List<Operator> operatorCategoryList = new ArrayList<>();

        if (entity == null) return operatorCategoryList;

        for (OperatorBannerEntity categoryDetailIncluded : entity.getOperators()) {
            Operator operatorCategory = new Operator();
            operatorCategory.setName(categoryDetailIncluded.getAttributes().getName());
            operatorCategory.setDefaultProductId(
                    categoryDetailIncluded.getAttributes().getDefaultProductId()
            );
            operatorCategory.setImage(categoryDetailIncluded.getAttributes().getImage());
            operatorCategory.setUssdCode(categoryDetailIncluded.getAttributes().getUssd());
            operatorCategory.setLastorderUrl(
                    categoryDetailIncluded.getAttributes().getLastorderUrl()
            );
            operatorCategory.setOperatorId(categoryDetailIncluded.getId());
            operatorCategory.setPrefixList(categoryDetailIncluded.getAttributes().getPrefix());
            operatorCategory.setOperatorType(categoryDetailIncluded.getType());
            List<Product> productOperatorList = new ArrayList<>();
            for (com.tokopedia.digital.common.data.entity.response.Product product
                    : categoryDetailIncluded.getAttributes().getProduct()) {
                if (product.getAttributes().getStatus() != Product.STATUS_INACTIVE) {
                    Product productOperator = new Product();
                    productOperator.setDesc(product.getAttributes().getDesc());
                    productOperator.setDetail(product.getAttributes().getDetail());
                    productOperator.setInfo(product.getAttributes().getInfo());
                    productOperator.setPrice(product.getAttributes().getPrice());
                    productOperator.setPricePlain(product.getAttributes().getPricePlain());
                    productOperator.setProductType(product.getType());
                    productOperator.setProductId(product.getId());
                    productOperator.setStatus(product.getAttributes().getStatus());
                    if (product.getAttributes().getPromo() != null) {
                        Promo productPromo = new Promo();
                        productPromo.setBonusText(product.getAttributes().getPromo().getBonusText());
                        productPromo.setId(product.getAttributes().getPromo().getId());
                        productPromo.setNewPrice(product.getAttributes().getPromo().getNewPrice());
                        productPromo.setNewPricePlain(
                                product.getAttributes().getPromo().getNewPricePlain()
                        );
                        productPromo.setTag(product.getAttributes().getPromo().getTag());
                        productPromo.setTerms(product.getAttributes().getPromo().getTerms());
                        productPromo.setValueText(product.getAttributes().getPromo().getValueText());
                        productOperator.setPromo(productPromo);
                    }
                    productOperatorList.add(productOperator);
                }
            }
            List<ClientNumber> clientNumberOperatorList = new ArrayList<>();
            for (Field field
                    : categoryDetailIncluded.getAttributes().getFields()) {
                ClientNumber clientNumberOperator = new ClientNumber();
                clientNumberOperator.setName(field.getName());
                clientNumberOperator.set_default(field.getDefault());
                clientNumberOperator.setType(field.getType());
                clientNumberOperator.setPlaceholder(field.getPlaceholder());
                clientNumberOperator.setText(field.getText());
                List<com.tokopedia.digital.product.view.model.Validation> validationCategoryList
                        = new ArrayList<>();
                for (com.tokopedia.digital.common.data.entity.response.Validation validation
                        : field.getValidation()) {
                    com.tokopedia.digital.product.view.model.Validation validationCategory =
                            new com.tokopedia.digital.product.view.model.Validation();
                    validationCategory.setError(validation.getError());
                    validationCategory.setRegex(validation.getRegex());
                    validationCategoryList.add(validationCategory);
                }
                clientNumberOperator.setValidation(validationCategoryList);
                clientNumberOperatorList.add(clientNumberOperator);
            }
            operatorCategory.setProductList(productOperatorList);
            operatorCategory.setClientNumberList(clientNumberOperatorList);

            Rule operatorRule = new Rule();
            operatorRule.setMaximumLength(
                    categoryDetailIncluded.getAttributes().getRule().getMaximumLength()
            );
            operatorRule.setEnableVoucher(
                    categoryDetailIncluded.getAttributes().getRule().getEnableVoucher()
            );
            operatorRule.setShowPrice(
                    categoryDetailIncluded.getAttributes().getRule().getShowPrice()
            );
            operatorRule.setProductText(
                    categoryDetailIncluded.getAttributes().getRule().getProductText()
            );
            operatorRule.setProductViewStyle(
                    categoryDetailIncluded.getAttributes().getRule().getProductViewStyle()
            );
            operatorRule.setButtonText(
                    categoryDetailIncluded.getAttributes().getRule().getButtonText()
            );
            operatorCategory.setRule(operatorRule);

            operatorCategoryList.add(operatorCategory);
        }

        return operatorCategoryList;
    }

    /**
     * Helper function to transform banner list
     *
     * @param entity
     * @return
     */
    private List<BannerData> transformBanners(List<OperatorBannerEntity> entity) {
        List<BannerData> bannerDataList = new ArrayList<>();

        if (entity != null) {
            for (OperatorBannerEntity bannerEntity : entity) {
                bannerDataList.add(
                        new BannerData.Builder()
                                .id(bannerEntity.getId())
                                .type(bannerEntity.getType())
                                .title(bannerEntity.getAttributes().getTitle())
                                .subtitle(bannerEntity.getAttributes().getSubTitle())
                                .promocode(bannerEntity.getAttributes().getPromocode())
                                .image(bannerEntity.getAttributes().getImage())
                                .dataTitle(bannerEntity.getAttributes().getDataTitle())
                                .link(bannerEntity.getAttributes().getLink())
                                .build()
                );
            }
        }

        return bannerDataList;
    }

}
