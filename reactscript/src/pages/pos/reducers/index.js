import { combineReducers } from 'redux'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  RELOAD_STATE,
} from '../actions/index'
import { bankData, emiData } from '../components/bankData'
import { shop } from './shop'
import { products } from './products'
import { cart } from './cart'
import { checkout } from './checkout'
import { paymentV2 } from './paymentv2'
import { payment } from './payment'
import { paymentRate } from './paymentRate'
import { historyTransaction } from './historyTransaction'
import { sendEmailResponse } from './sendEmailResponse'
import { search } from './search'
import { etalase } from './etalase'



const appReducer = combineReducers({
  products,
  etalase,
  cart,
  payment,
  checkout,
  paymentV2,
  search,
  historyTransaction,
  shop,
  paymentRate,
  sendEmailResponse
})

const rootReducer = (state, action) => {
  if (action.type === 'RELOAD_STATE') {
    state = undefined
  }
  return appReducer(state, action)
}

export default rootReducer