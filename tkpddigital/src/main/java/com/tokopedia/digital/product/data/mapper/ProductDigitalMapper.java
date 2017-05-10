package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.data.entity.ResponseBanner;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.product.data.entity.response.ResponseLastOrderData;
import com.tokopedia.digital.product.data.entity.response.ResponseRecentNumberData;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.model.Promo;
import com.tokopedia.digital.product.model.Rule;
import com.tokopedia.digital.product.model.Teaser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalMapper implements IProductDigitalMapper {
    @Override
    public BannerData transformBannerData(ResponseBanner responseBanner)
            throws MapperDataException {
        return new BannerData.Builder()
                .type(responseBanner.getType())
                .id(responseBanner.getId())
                .rechargeCmsbannerId(responseBanner.getAttributes().getRechargeCmsbannerId())
                .fileName(responseBanner.getAttributes().getFileName())
                .fileNameWebp(responseBanner.getAttributes().getFileNameWebp())
                .startDate(responseBanner.getAttributes().getStartDate())
                .endDate(responseBanner.getAttributes().getEndDate())
                .imgUrl(responseBanner.getAttributes().getImgUrl())
                .priority(responseBanner.getAttributes().getPriority())
                .status(responseBanner.getAttributes().getStatus())
                .title(responseBanner.getAttributes().getTitle())
                .subtitle(responseBanner.getAttributes().getSubtitle())
                .promocode(responseBanner.getAttributes().getPromocode())
                .dataTitle(responseBanner.getAttributes().getDataTitle())
                .build();
    }

    @Override
    public CategoryData transformCategoryData(
            ResponseCategoryDetailData responseCategoryDetailData,
            List<ResponseCategoryDetailIncluded> responseCategoryDetailIncludedList
    ) throws MapperDataException {
        CategoryData categoryData = new CategoryData();
        categoryData.setCategoryId(responseCategoryDetailData.getId());
        categoryData.setCategoryType(responseCategoryDetailData.getType());
        categoryData.setDefaultOperatorId(responseCategoryDetailData.getAttributes().getDefaultOperatorId());
        categoryData.setIcon(responseCategoryDetailData.getAttributes().getIcon());
        categoryData.setIconUrl(responseCategoryDetailData.getAttributes().getIconUrl());
        categoryData.setName(responseCategoryDetailData.getAttributes().getName());
        categoryData.setInstantCheckout(responseCategoryDetailData.getAttributes().isInstantCheckout());
        categoryData.setNew(responseCategoryDetailData.getAttributes().isNew());
        categoryData.setSlug(responseCategoryDetailData.getAttributes().getSlug());
        categoryData.setOperatorStyle(responseCategoryDetailData.getAttributes().getOperatorStyle());

        List<ClientNumber> clientNumberCategoryList = new ArrayList<>();
        for (com.tokopedia.digital.product.data.entity.response.Field field
                : responseCategoryDetailData.getAttributes().getFields()) {
            if (field.getName().equalsIgnoreCase("client_number")) {
                ClientNumber clientNumberCategory = new ClientNumber();
                clientNumberCategory.setName(field.getName());
                clientNumberCategory.set_default(field.get_default());
                clientNumberCategory.setType(field.getType());
                clientNumberCategory.setHelp(field.getHelp());
                clientNumberCategory.setPlaceholder(field.getPlaceholder());
                clientNumberCategory.setText(field.getText());
                List<com.tokopedia.digital.product.model.Validation> validationCategoryList
                        = new ArrayList<>();
                for (com.tokopedia.digital.product.data.entity.response.Validation validation
                        : field.getValidation()) {
                    com.tokopedia.digital.product.model.Validation validationCategory =
                            new com.tokopedia.digital.product.model.Validation();
                    validationCategory.setError(validation.getError());
                    validationCategory.setRegex(validation.getRegex());
                    validationCategoryList.add(validationCategory);
                }
                clientNumberCategory.setValidation(validationCategoryList);
                clientNumberCategoryList.add(clientNumberCategory);
            }
        }
        categoryData.setClientNumberList(clientNumberCategoryList);

        List<Operator> operatorCategoryList = new ArrayList<>();
        for (ResponseCategoryDetailIncluded categoryDetailIncluded : responseCategoryDetailIncludedList) {
            if (categoryDetailIncluded.getType().equalsIgnoreCase("operator")) {
                Operator operatorCategory = new Operator();
                operatorCategory.setName(categoryDetailIncluded.getAttributes().getName());
                operatorCategory.setDefaultProductId(categoryDetailIncluded.getAttributes().getDefaultProductId());
                operatorCategory.setImage(categoryDetailIncluded.getAttributes().getImage());
                operatorCategory.setLastorderUrl(categoryDetailIncluded.getAttributes().getLastorderUrl());
                operatorCategory.setOperatorId(categoryDetailIncluded.getId());
                operatorCategory.setPrefixList(categoryDetailIncluded.getAttributes().getPrefix());
                operatorCategory.setOperatorType(categoryDetailIncluded.getType());
                List<Product> productOperatorList = new ArrayList<>();
                for (com.tokopedia.digital.product.data.entity.response.Product product
                        : categoryDetailIncluded.getAttributes().getProduct()) {
                    Product productOperator = new Product();
                    productOperator.setDesc(product.getAttributes().getDesc());
                    productOperator.setDetail(product.getAttributes().getDetail());
                    productOperator.setInfo(product.getAttributes().getInfo());
                    productOperator.setPrice(product.getAttributes().getPrice());
                    productOperator.setPricePlain(product.getAttributes().getPricePlain());
                    productOperator.setProductType(product.getType());
                    productOperator.setProductId(product.getId());
                    if (product.getAttributes().getPromo() != null) {
                        Promo productPromo = new Promo();
                        productPromo.setBonusText(product.getAttributes().getPromo().getBonusText());
                        productPromo.setId(product.getAttributes().getPromo().getId());
                        productPromo.setNewPrice(product.getAttributes().getPromo().getNewPrice());
                        productPromo.setNewPricePlain(product.getAttributes().getPromo().getNewPricePlain());
                        productPromo.setTag(product.getAttributes().getPromo().getTag());
                        productPromo.setTerms(product.getAttributes().getPromo().getTerms());
                        productPromo.setValueText(product.getAttributes().getPromo().getValueText());
                        productOperator.setPromo(productPromo);
                    }
                    productOperatorList.add(productOperator);

                }
                List<ClientNumber> clientNumberOperatorList = new ArrayList<>();
                for (com.tokopedia.digital.product.data.entity.response.Field field
                        : categoryDetailIncluded.getAttributes().getFields()) {
                    ClientNumber clientNumberOperator = new ClientNumber();
                    clientNumberOperator.setName(field.getName());
                    clientNumberOperator.set_default(field.get_default());
                    clientNumberOperator.setType(field.getType());
                    clientNumberOperator.setHelp(field.getHelp());
                    clientNumberOperator.setPlaceholder(field.getPlaceholder());
                    clientNumberOperator.setText(field.getText());
                    List<com.tokopedia.digital.product.model.Validation> validationCategoryList
                            = new ArrayList<>();
                    for (com.tokopedia.digital.product.data.entity.response.Validation validation
                            : field.getValidation()) {
                        com.tokopedia.digital.product.model.Validation validationCategory =
                                new com.tokopedia.digital.product.model.Validation();
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
                operatorRule.setEnableVoucher(categoryDetailIncluded.getAttributes().getRule().isEnableVoucher());
                operatorRule.setShowPrice(categoryDetailIncluded.getAttributes().getRule().isShowPrice());
                operatorRule.setProductText(categoryDetailIncluded.getAttributes().getRule().getProductText());
                operatorRule.setProductViewStyle(categoryDetailIncluded.getAttributes().getRule().getProductViewStyle());
                operatorCategory.setRule(operatorRule);

                operatorCategoryList.add(operatorCategory);
            }
        }
        categoryData.setOperatorList(operatorCategoryList);

        if (responseCategoryDetailData.getAttributes().getTeaser() != null) {
            Teaser categoryTeaser = new Teaser();
            categoryTeaser.setContent(responseCategoryDetailData.getAttributes().getTeaser().getContent());
            categoryTeaser.setTitle(responseCategoryDetailData.getAttributes().getTeaser().getTitle());
            categoryData.setTeaser(categoryTeaser);
        }
        return categoryData;
    }

    @Override
    public OrderClientNumber transformOrderClientNumber(
            ResponseLastOrderData lastOrderData
    ) throws MapperDataException {
        return new OrderClientNumber.Builder()
                .clientNumber(lastOrderData.getAttributes().getClientNumber())
                .categoryId("")
                .operatorId(String.valueOf(lastOrderData.getAttributes().getOperatorId()))
                .productId(String.valueOf(lastOrderData.getAttributes().getProductId()))
                .build();
    }

    @Override
    public OrderClientNumber transformOrderClientNumber(
            ResponseRecentNumberData responseRecentNumberData
    ) throws MapperDataException {
        return new OrderClientNumber.Builder()
                .clientNumber(responseRecentNumberData.getAttributes().getClientNumber())
                .productId("")
                .categoryId(responseRecentNumberData.getRelationships().getCategory().getData().getId())
                .operatorId("")
                .build();
    }
}
