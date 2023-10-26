---
labels:
- SRP
- Search
---
<!--left header table-->
| **Status**      | <!--start status:Green-->RELEASE<!--end status-->                  |
|-----------------|--------------------------------------------------------------------|
| Team            | Minion Dave                                                        |
| Module type     | <!--start status:Yellow-->FEATURE<!--end status-->                 |
| Release date    | -                                                                  |
| Product Manager | @Hefrian, @Sheila, @Gilang                                         |
| Contributors    | @Zulfikar Rahman @Darian Thedy @Willybrodus                        |
| Module Location | features.search / features/discovery/search / com.tokopedia.search |
| Product PRD     | -                                                                  |

<!--toc-->

## Overview

### Background
During Product Card Reimagine, there is a new contract decided to re-group related fields and streamline the data required by the Product Card. Field names are also changed to make it more standardized, such as `url` into `URL` and using camel case.

We also introduced and defined a field called `meta`, which means every field inside this object will not be shown directly to the user, and used only as configurations or tracking. For example, field `products.meta` means a Product data that will not be shown to the user, such as `warehouseID` or `componentID`.

This new contract will be provided in new GQL query `searchProductV5`.

### Project Description
Migrate Search Product into new GQL query `searchProductV5` using Rollence as toggle. Experiment name is: `search_3_pc`.

| GQL Query               | Variant name                           |
|-------------------------|----------------------------------------|
| `searchProductV5`       | `var_1a`, `var_1b`, `var_2a`, `var_2b` |
| `ace_search_product_v4` | `control_variant`                      |

For new GQL, we wil put the response into a new model `SearchProductV5`, and this means a few functionalities need to be mapped from this class into the existing UiModel / DataView if we are using GQL Query `searchProductV5`. Currently, these functionalities are:
1. Adult Popup (`isQuerySafe`)
2. Banner (`banner`)
3. Redirection (`redirection`)
4. Ticker (`ticker`)
5. Suggestion (`suggestion`)
6. Broad Match (`related`)
7. Violation (`violation`)
8. Product list (`products`)

## Action Items
After the Rollence for Search Reimagine is over, we need to fully migrate into `searchProductV5`.
1. Need to check and delete everything using boolean from `ReimagineRollence.search3ProductCard.isUseAceSearchProductV5()`.
2. Need to change all mock response from unit test and instrumentation test from `ace_search_product_v4` into `searchProductV5`.


## Useful Links
1. [Search Product V5](https://tokopedia.atlassian.net/wiki/spaces/SE/pages/2361393529/GQL+Search+Product+V5+Reimagine)