import axios from 'axios'
import {
  PosCacheModule,
  NetworkModule,
  SessionModule,
  PaymentModule,
  UtilModule
} from 'NativeModules'
import { 
  BASE_API_URL_PAYMENT,
  BASE_API_URL_SCROOGE,
  BASE_API_URL_PCIDSS
} from '../lib/api.js'



// ===================== Product List ======================= //
export const FETCH_PRODUCTS = 'FETCH_PRODUCTS'
export const fetchProducts = (shopId, start, rows, etalaseId, productId, queryText) => {
  return {
    type: FETCH_PRODUCTS,
    payload: PosCacheModule.getDataAll("PRODUCT")
      .then(response => {
        const jsonResponse = JSON.parse(response)
        return jsonResponse;
      })
      .catch(error => {})
  }
}

// ==================== Search Product ==================== //
export const SEARCH_PRODUCT = 'SEARCH_PRODUCT'
export const searchProduct = (product, etalaseId) => {
  return {
    type: SEARCH_PRODUCT,
    payload: ProductDiscoveryModule.search(`{ product: ${product}, etalase_id: ${etalaseId} }`)
                .then(res => {
                  const jsonResponse = JSON.parse(res)
                  console.log(jsonResponse)
                  return jsonResponse
                })
                .catch(err => {})
  }
}


export const FETCH_SHOP_NAME = 'FETCH_SHOP_NAME'
export const fetchShopName = () => ({
  type: FETCH_SHOP_NAME,
  payload: SessionModule.getShopName()
    .then(res => {
      return res
    })
    .catch(err => console.log(err))
})

export const FETCH_SHOP_ID = 'FETCH_SHOP_ID'
export const fetchShopId = () => ({
  type: FETCH_SHOP_ID,
  payload: SessionModule.getShopId()
})

export const FETCH_ETALASE = 'FETCH_ETALASE'
export const fetchEtalase = (shopId) => ({
  type: FETCH_ETALASE,
  payload: PosCacheModule.getDataAll("ETALASE")
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
    })
    .catch(err => console.log(err))
})


export const PULL_TO_REFRESH = 'PULL_TO_REFRESH'
export const pullToRefresh = () => {
  return {
    type: PULL_TO_REFRESH
  }
}

export const ON_ETALASE_CHANGE = 'ON_ETALASE_CHANGE'
export const onEtalaseChange = (id) => {
  return {
    type: ON_ETALASE_CHANGE,
    payload: id
  }
}

export const RESET_PRODUCT_LIST = 'RESET_PRODUCT_LIST'
export const resetProductList = () => {
  return {
    type: RESET_PRODUCT_LIST,
  }
}

// // Cart actions and action creators
// export const ADD_TO_CART = 'ADD_TO_CART'
// export const addToCart = (item) => {
//   return {
//     type: ADD_TO_CART,
//     payload: item,
//   }
// }

// ====================== Yogie - 18 September 2017 ===================== //
// Fetch Cart From Local Native Cache
export const FETCH_CART_FROM_CACHE = 'FETCH_CART_FROM_CACHE'
export const fetchCartFromCache = () => {
  return {
    type: FETCH_CART_FROM_CACHE,
    payload: fetchCart()
  }
}

const fetchCart = () => {
  return PosCacheModule.getDataAll('CART')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      return jsonResponse.data;
    })
    .catch(error => console.log(error))
}


// ===================== Make Payment ===================== //
export const PAYMENT_CHECKOUT_TO_NATIVE = 'PAYMENT_CHECKOUT_TO_NATIVE'
export const PaymentCheckoutToNative = () => {
  return {
    type: PAYMENT_CHECKOUT_TO_NATIVE,
    payload: doPayment()
  }
}

const doPayment = async () => {
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  const shop_id = await getShopId()
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const local_cart = await getCart()

  const data_payment = {
    base_api_url_payment: api_url.api_url_payment,
    base_api_url_scrooge: api_url.api_url_scrooge,
    base_api_url_pcidss: api_url.api_url_pcidss,
    user_id: parseInt(user_id),
    os_type: '1',
    addr_id: parseInt(addr_id),
    client_id: '',
    shop_id: parseInt(shop_id),
    cart: local_cart
  }
  // console.log(data_payment)

  const paymentToNative_getParams = await makePaymentToNativeStepOne(data_payment)
  // console.log(paymentToNative_getParams)  
  
  const paymentToNative_secondStep = await makePaymentToNativeStepTwo(paymentToNative_getParams, data_payment, local_cart)
  // console.log(paymentToNative_secondStep)

  return paymentToNative_secondStep
}

const getUserId = () => {
  return SessionModule.getUserId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getAddrId = () => {
  return SessionModule.getAddrId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getShopId = () => {
  return SessionModule.getShopId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getCart = async () => {
  let objData = []
  const cart = await fetchCart()
  
  cart.list.map((data) => {
    objData.push({
      product_id: data.product_id,
      quantity: data.quantity
    })
  })

  return objData
}

const getEnv = () => {
  return SessionModule.getEnv()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getBaseAPI = (env) => {
  let data_api = {}

  if (env === 'production'){
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`
    }
    return data_api

  } else {
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`
    }
    return data_api
  }
}

const makePaymentToNativeStepOne = (data_payment) => {
  const data = {
    user_id: data_payment.user_id,
    os_type: data_payment.os_type,
    addr_id: data_payment.addr_id,
    client_id: data_payment.client_id,
    shop_id: data_payment.shop_id,
    cart: data_payment.cart
  }

  return NetworkModule.getResponseJson(`${data_payment.base_api_url_payment}/o2o/get_payment_params`, `POST`, JSON.stringify(data), true)
    .then(response => {
      const jsonResponse = JSON.parse(response)
      let itemsData = []

      jsonResponse.data.parameter.items.map((res) => {
        itemsData.push({ 
          id: res.id,
          name: res.name,
          price: res.price,
          quantity: res.quantity
        })
      })
      return { jsonResponse, itemsData}
    })
    .catch(err => { console.log(err) })
}
// ===================== Make Payment ===================== //


// ===================== Make Payment 2 ===================== //
const makePaymentToNativeStepTwo = (paymentToNative_getParams, data_payment, local_cart) => {
  let data_items = ''
  paymentToNative_getParams.itemsData.map((res) => {
    data_items += `items[name]=${encodeURIComponent(res.name)}&items[quantity]=${encodeURIComponent(res.quantity)}&items[price]=${encodeURIComponent(res.price)}&`
  })
  
  const data_length = data_items.length
  const data_cleaned = data_items.substr(0, data_length - 1)
  const dataParams = paymentToNative_getParams.jsonResponse.data.parameter
  const data_qry_params = `merchant_code=${encodeURIComponent(dataParams.merchant_code)}&profile_code=${encodeURIComponent(dataParams.profile_code)}&` +
      `transaction_id=${encodeURIComponent(dataParams.transaction_id)}&transaction_date=${encodeURIComponent(dataParams.transaction_date)}&` +
      `currency=${encodeURIComponent(dataParams.currency)}&amount=${encodeURIComponent(dataParams.amount)}&customer_name=${encodeURIComponent(dataParams.customer_name)}&` + 
      `customer_email=${encodeURIComponent(dataParams.customer_email)}&user_defined_value=${encodeURIComponent(dataParams.user_defined_value)}&` +
      `payment_metadata=${encodeURIComponent(dataParams.payment_meta)}&signature=${encodeURIComponent(dataParams.signature)}&`
  const query_params = data_qry_params + '' + data_cleaned

  return NetworkModule.getResponseParam(`${data_payment.base_api_url_scrooge}/v1/api/payment`, `POST`, query_params, false)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
    })
    .catch(err => { console.log(err) })
}
// ===================== Make Payment 2 ===================== //




//  ==================== Remove 1 item inside Cart ===================== //
export const REMOVE_FROM_CART = 'REMOVE_FROM_CART'
export const removeFromCart = (pid) => {
  return {
    type: REMOVE_FROM_CART,
    payload: PosCacheModule.deleteItem('CART', pid.toString())
      .then(response => {
        const jsonResponse = JSON.parse(response)
        console.log(response)
        if (jsonResponse.data.status) {
          return { pid }
        }
      })
      .catch(error => console.log(error))
  }
}

//  ==================== Increment Quantity inside Cart ===================== //
export const INCREMENT_QTY = 'INCREMENT_QTY'
export const incrementQty = (id, pid, qty) => {
  const quantity = qty + 1

  return {
    type: INCREMENT_QTY,
    payload: PosCacheModule.update('CART', `{id:${id}, product_id:${pid}, quantity:${quantity}}`)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        if (jsonResponse.data.status) {
          return { id, pid, quantity }
        }
      })
      .catch(error => console.log(error))
  }
}

//  ==================== Decrement Quantity inside Cart ===================== //
export const DECREMENT_QTY = 'DECREMENT_QTY'
export const decrementQty = (id, pid, qty) => {
  const quantity = qty - 1

  return {
    type: DECREMENT_QTY,
    payload: PosCacheModule.update('CART', `{id:${id}, product_id:${pid}, quantity:${quantity}}`)
      .then(response => {
        const jsonResponse = JSON.parse(response)
        if (jsonResponse.data.status) {
          return { id, pid, quantity }
        }
      })
      .catch(error => console.log(error))
  }
}


//  ==================== Clear all Data inside Cart ===================== //
export const CLEAR_CART = 'CLEAR_CART'
export const clearCart = () => {
  return {
    type: CLEAR_CART,
    payload: PosCacheModule.deleteAll('CART')
      .then(response => { })
      .catch(error => console.log(error))
  }
}



export const BANK_SELECTED = 'BANK_SELECTED'
export const selectBank = (id) => {
  return {
    type: BANK_SELECTED,
    payload: id
  }
}

//  ==================== Fetch data Bank from Cache ===================== //
export const FETCH_BANK_FUlFILLED = 'FETCH_BANK_FUlFILLED'
export const getBankList = () => {
  return {
    type: FETCH_BANK_FUlFILLED,
    payload: fetchBankData()
  }
}

const fetchBankData = () => {
  return PosCacheModule.getDataAll('BANK')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      console.log(jsonResponse.data.list)
      if (jsonResponse.data) return jsonResponse.data.list
    })
    .catch(err => console.log(err))
}

//  ==================== Fetch data Installment from Cache ===================== //
export const FETCH_EMI_FUlFILLED = 'FETCH_EMI_FUlFILLED'
export const getEmiList = () => {
  return {
    type: FETCH_EMI_FUlFILLED,
    payload: fetchBankData()
  }
}

export const SELECT_PAYMENT_OPTIONS = 'SELECT_PAYMENT_OPTIONS'
export const selectPaymentOptions = (option, value) => {
  return {
    type: SELECT_PAYMENT_OPTIONS,
    payload: { option: option, value: value }
  }
}



export const EMI_SELECTED = 'EMI_SELECTED'
export const selectEmi = (id) => {
  return {
    type: EMI_SELECTED,
    payload: id
  }
}


//  ==================== Make Payment to Native ===================== //
export const MAKE_PAYMENT = 'MAKE_PAYMENT'
export const makePayment = (total_amount, installment_term, cc_no, expiry_date, cvv) => {
  const data = { total_amount, installment_term, cc_no, expiry_date, cvv }

  return {
    type: MAKE_PAYMENT,
    payload: postDataToNative(data)
  }
}

const postDataToNative = (data) => {
  return PaymentModule.pay(`{ 
    total_amount: ${data.total_amount},
    installment_term: ${data.installment_term},
    cc_no: ${data.cc_no},
    expiry_date: ${data.expiry_date},
    cvv: ${data.cvv}
  }`).then(response => {
      console.log(response)
    })
    .catch(err => {
      console.log(err)
    })
}



// Search actions
export const ON_SEARCH_QUERY_TYPE = 'ON_SEARCH_QUERY_TYPE'
export const onSearchQueryType = (queryText) => {
  return {
    type: ON_SEARCH_QUERY_TYPE,
    payload: queryText
  }
}

export const FETCH_SEARCH_PRODUCT = 'FETCH_SEARCH_PRODUCT'
export const fetchSearchProduct = (eId, queryText, shopId) => {
  const text = queryText.replace(' ', '+')
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=5&shop_id=${shopId}&start=0&q=${text}`

  const etalaseId = +eId || 0
  if (etalaseId) {
    url += `&etalase=${etalaseId}`
  }
  return {
    type: FETCH_SEARCH_PRODUCT,
    payload: axios.get(url),
    queryText: queryText,
  }
}

export const ON_SEARCH_RESULT_TAP = 'ON_SEARCH_RESULT_TAP'
export const onSearchResultTap = () => {
  return {
    type: ON_SEARCH_RESULT_TAP,
  }
}

export const CLEAR_SEARCH_RESULTS = 'CLEAR_SEARCH_RESULTS'
export const clearSearchResults = () => {
  return {
    type: CLEAR_SEARCH_RESULTS,
  }
}

export const SET_SEARCH_TEXT = 'SET_SEARCH_TEXT'
export const setSearchText = (q) => {
  return {
    type: SET_SEARCH_TEXT,
    payload: q,
  }
}

export const ON_SUBMIT_FETCH_SEARCH_PRODUCT = 'ON_SUBMIT_FETCH_SEARCH_PRODUCT'
export const onSubmitFetchSearchProduct = (queryText, eId, shopId) => {
  const text = queryText.replace(' ', '+')
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=25&shop_id=${shopId}&start=0&q=${text}`
  const etalaseId = +eId || 0
  if (etalaseId) {
    url += `&etalase=${etalaseId}`
  }
  return {
    type: ON_SUBMIT_FETCH_SEARCH_PRODUCT,
    payload: axios.get(url),
    queryText: queryText,
  }
}

export const FETCH_TRANSACTION_HISTORY = 'FETCH_TRANSACTION_HISTORY'
export const getTransactionHistory = () => {
  return {
    type: FETCH_TRANSACTION_HISTORY,
  }
}