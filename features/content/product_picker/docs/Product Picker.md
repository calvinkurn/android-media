---
title: "Product Picker"
labels:
- product-picker
- content
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 03 Nov 2023 / <!--start status:GREY-->MA-3.241<!--end status--> <!--start status:GREY-->SA-2.171<!--end status-->  |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status--> |
| Module Location | `features.contentProductPicker` | `features/content/product_picker` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:15 Dec 2023 (MA-3.247/SA-2.177)-->
Changes to support Manual Stories

<https://github.com/tokopedia/android-tokopedia-core/pull/35346>
<!--end expand-->

<!--start expand:03 Nov 2023 (MA-3.241/SA-2.171)-->
Initial Release

<https://github.com/tokopedia/android-tokopedia-core/pull/35346>
<!--end expand-->

## Overview

`Product picker` in content is a module that stores all the product-picker-related logic, including product chooser, product filter & sort, and product summary. This module is used in CCP (Content Creation Platform) features e.g. post feed, livestream, shorts video, and stories creation. For now, only seller product picker journey that has included in this module, while the ugc journey will be picked up later.

### Background

Product picker is used across CCP features (post feed, livestream, shorts video, stories) and it has the same flow & logic. To make it reusable, we decided to create a new dedicated module that handle the product picking journey within CCP features.

### Project Description

Ideally, this module will handle 2 product picking journey:

- Product picker for SGC (seller)
- Product picker for UGC (user)

These 2 types of user has some differences that are described in the table below:



| **Description** | **Product Picker SGC (seller)** | **Product Picker UGC (user)** |
| --- | --- | --- |
| Product source | - Shop’s etalase<br/>- Shop’s campaign<br/> | - Last tagged product<br/>- Last purchased product<br/>- Wishlist<br/>- Global Search<br/>- Shop’s etalase (if user has a shop)<br/> |
| Has breadcrumb | ❌ | ✅ |
| Has sample activity (for development & testing purpose only) | ❌ | ✅ |

For now, we only have **Product Picker SGC included in this module** and will add Product Picker UGC later.

**Product Picker SGC** has 4 main bottom sheets:



| **Bottom Sheet** | **Description** |
| --- | --- |
| Product Chooser Bottom Sheet | For choosing products |
| Product Sort Bottom Sheet | For sorting product list |
| Product Etalase Filter Bottom Sheet | For filtering product list based on selected campaign / etalase |
| Product Summary Bottom Sheet | For displaying selected products by user |

Here is some screenshots about **Product Picker SGC:**

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231124-040130.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231124-040153.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231124-040209.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20231124-040238.png" alt="" />

**Product Picker SGC** is configurable to cater different content creation use cases, here is the things that you can configure:



| **Configurable** | **Description** |
| --- | --- |
| GQL | - `ContentProductPickerSellerRepository`<br/>	- Product Etalase List<br/>	- Set Product Tag<br/>	- Get Product Tag Summary<br/>	- Set Pin Product<br/> |
| Analytics | - `ContentProductPickerSellerAnalytic`<br/>	- End-to-end product picker journey<br/>- `ContentPinnedProductAnalytic`<br/>	- Pinned product<br/> |

## Tech Stack

- **Fragment Factory** : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor. [Click here to see the official documentation.](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory)
- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **Adapter Delegate** : We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood. [Click here to see the official documentation.](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/697903385)
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- **Flow** : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only. [Click here to see the official documentation.](https://developer.android.com/kotlin/flow)

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Content%20-%20Product%20Picker%20Flow.drawio-20231129-100204.png" alt="" />

## How-to

1. Add product\_picker dependency in `build.gradle`



```
dependencies {
  ...
  implementation projectOrAar(rootProject.ext.features.contentProductPicker)
  ...
}
```
2. Setup your DI by including these 3 modules.



```
@Component(
    modules = [
        ...
        /** Add these 3 modules */
        ProductPickerFragmentModule::class,
        ProductPickerBindModule::class,
        ContentFragmentFactoryModule::class,
        ....
    ],
    dependencies = [BaseAppComponent::class]
)
interface YourComponent {
    ...
}
```
3. Setup Repository & Analytics Implementations



```
@Module
abstract class YourModule {

  // TODO 1: provide ContentProductPickerSellerRepository implementation
  @Binds
  abstract fun bindContentProductPickerSellerRepository(yourRepositoryImpl: YourImpl): ContentProductPickerSellerRepository
  
  // TODO 2: provide ContentProductPickerSellerAnalytic implementation
  @Binds
  abstract fun bindContentProductPickerSellerAnalytic(yourAnalyticImpl: YourAnalyticImpl): ContentProductPickerSellerAnalytic
  
  // TODO 3: provide ContentPinnedProductAnalytic implementation
  @Binds
  abstract fun bindContentPinnedProductAnalytic(yourPinProductAnalyticImpl: YourPinProductAnalyticImpl): ContentPinnedProductAnalytic
}
```
4. Setup your FragmentFactory.



```
class YourActivity : AppCompatActivity() {
  
  @Inject
  lateinit var fragmentFactory: FragmentFactory
  
  override fun onCreate(savedInstanceState: Bundle?) {
      supportFragmentManager.fragmentFactory = fragmentFactory
      super.onCreate(savedInstanceState)
      ...
  }
}
```
5. Launch Product Picker SGC.



```
class YourActivity : AppCompatActivity() {
    private fun openProductPickerSGC() {
      supportFragmentManager.beginTransaction()
        .add(ProductSetupFragment::class.java, null, null)
        .commit()
    }
}
```
6. Setup listener & data after Product Picker SGC fragment is successfully attached.



```
class YourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupAttachFragmentListener()
    }
    
    private fun setupAttachFragmentListener() {
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            when (fragment) {
                is ProductSetupFragment -> {
                    fragment.setDataSource(object : ProductSetupFragment.DataSource {
                        override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                            // TODO 1: provide selected product list
                        }

                        override fun isEligibleForPin(): Boolean {
                            // TODO 2: provide isEligibleForPin
                        }

                        override fun getSelectedAccount(): ContentAccountUiModel {
                            // TODO 3: provide selected account, should be shop account
                        }

                        override fun creationId(): String {
                            // TODO 4: provide contentCreationId (e.g. channelId, storiesId)
                        }

                        override fun maxProduct(): Int {
                            // TODO 5: provide max product allowed
                        }

                        override fun isNumerationShown(): Boolean {
                            // TODO 6: provide isNumerationShown
                        }

                        override fun fetchCommissionProduct(): Boolean {
                            // TODO 7: provide fetchCommissionProduct
                        }
                    })
                    
                    fragment.setListener(object : ProductSetupFragment.Listener {
                        override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
                            // TODO 8: handle when products are successfully picked by user
                        }
                    })
                }
            }
        }
    }
}
```



---

## Action Items

- Move **Product Picker SGC** from `content_common` to `product_picker`

## Useful Links

- Product Picker UGC  
[[Android] Content Product Picker UGC](/wiki/spaces/PA/pages/2054263530)

## FAQ

<!--start expand:Can I reuse this product picker in module outside content?-->
We are not encourage you to do so since this product picker is built based on CCP use case. But please let us know if you have another opinions.
<!--end expand-->

