## Description

## Phab Link
https://phab.tokopedia.com/XXXXX

## Types of changes
<!--- What types of changes does your code introduce? Put an `x` in all the boxes that apply: -->
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to change)

## Code Quality Checklist
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] removed commented and unused code
- [ ] using the module as prefix before new resource name to avoid conflict
- [ ] using the unify design component wherever applicable

## QA Checklist
- [ ] Tested on pre lollipop devices
- [ ] Tested on nougat and above devices
- [ ] using compressed images and webp/vector wherever applicable
- [ ] Tested on signed build
- [ ] Tested your changes with Don't keep activities options enabled in settings
- [ ] Added unit test cases
- [ ] My changes are not increasing the size more than 50kb
- [ ] Hansel Integrated if any new module integrated
- [ ] Firebase toggle added (mandatory needed for new features)

## List any new permission added in manifest

## List any new/updated third party libraries

## Affected Module / Feature

## QA Name

## Command List
- **/check all** - run all pr checker
- **/check build** - run MainApp and/or SellerApp checker based on affected modules
- **/check unittest** - run unit test checker
- **/check review** - run auto mention code review system
- **/check risk** - run risk analysis
- **/check lint** - run linter checker
- **/check instrument** - run TopAds Checker instrumentation test
- **/check datatest** - run data tracking test (Cassava)
- **/check performance** - run performance check
- **/check katalon** - include this PR in katalon auto smoke test next batch (midnight same day)

## Gatekeeper Spells
- **approved for app-size** - send along with the approval to cast the magic 
*(more info: @tokopedia/android-gatekeeper-app-size)*

- **approved for risk** - send along with the approval to cast the magic 
*(more info: @tokopedia/android-gatekeeper-risk-analysis)*

- **approved for linter** - send along with the approval to cast the magic 
*(more info: @tokopedia/android-gatekeeper-linter)*

- **approved for new module** - send along with the approval to cast the magic 
*(more info: @tokopedia/android-gatekeeper-new-module)*

- **approved for dark mode** - send along with the approval to cast the magic
*(more info: @tokopedia/android-gatekeeper-dark-mode)*

