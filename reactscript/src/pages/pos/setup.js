import React, { Component } from 'react'
// import { View, Text } from 'react-native'
import Setup_pos from './setup_pos'
import Setup_payment from './setup_payment'
import Setup_cart from './setup_cart'
import Setup_processing from './setup_processing'
import Setup_history from './setup_history'
import DetailCicilan from './setup_cicilan'
import Invoice from './setup_invoice'


class Root extends Component {
  render() {
    const { POS_PAGE } = this.props.data
    if (POS_PAGE === 'POS'){
      return <Setup_pos />
    } else if (POS_PAGE === 'PAYMENT'){
      return <Setup_payment data={this.props.data} />
    } else if (POS_PAGE === 'LOCAL_CART'){
      return <Setup_cart />
    } else if (POS_PAGE === 'PROCESSING'){
      return <Setup_processing data={this.props.data} />
    } else if (POS_PAGE === 'HISTORY'){
      return <Setup_history />
    } else if (POS_PAGE === 'CICILAN'){
      return <DetailCicilan />
    } else if (POS_PAGE === 'INVOICE'){
      return <Invoice data={this.props.data} />
    }
  }
}
export default Root