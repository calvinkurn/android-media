import React, { Component } from 'react'

import SuccessPage from './pages/SuccessPage'
import TransferPage from './pages/TransferPage'

class Root extends Component {
  componentDidMount(){
    console.log(this.props)
  }


  render() {
    const { template } = this.props.data
    
    if (template === 'transfer'){
      return <TransferPage data={this.props.data}/>
    } else if (template === 'instant') {
      return <SuccessPage data={this.props.data}/>
    }
    // if (template === 'transfer'){
    //   return <SuccessPage data={this.props.data} />
    // } else {
    //   return <TransferPage data={this.props.data} />
    // }
  }
}

export default Root
